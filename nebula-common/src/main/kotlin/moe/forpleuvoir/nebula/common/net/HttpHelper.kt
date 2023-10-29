package moe.forpleuvoir.nebula.common.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpClient.Version
import java.net.http.HttpRequest.*
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandler
import java.time.Duration
import java.util.*

@Suppress("KDocUnresolvedReference", "UNUSED", "MemberVisibilityCanBePrivate")
class HttpHelper<T>(
	private val uri: String,
	private val bodyHandler: BodyHandler<T>,
	internal var bodyPublisher: BodyPublisher = BodyPublishers.noBody(),
) {

	internal val client = HttpClient.newHttpClient()

	internal val requestBuilder: Builder = newBuilder().uri(URI.create(uri)).timeout(Duration.ofSeconds(10))

	/**
	 * 设置超时 默认 10秒
	 * @param time Duration
	 */
	fun timeout(time: Duration): HttpHelper<T> {
		requestBuilder.timeout(time)
		return this
	}

	/**
	 * 设置超时 默认 10秒
	 * @param time Duration
	 */
	inline fun timeout(time: () -> Duration): HttpHelper<T> {
		return timeout(time())
	}

	/**
	 * 请求体
	 * @param bodyPublisher Function0<BodyPublisher>
	 * @return HttpHelper<T>
	 */
	fun bodyPublisher(bodyPublisher: BodyPublisher): HttpHelper<T> {
		this.bodyPublisher = bodyPublisher
		return this
	}

	/**
	 * 请求体
	 * @param bodyPublisher Function0<BodyPublisher>
	 * @return HttpHelper<T>
	 */
	inline fun bodyPublisher(bodyPublisher: () -> BodyPublisher): HttpHelper<T> {
		return bodyPublisher(bodyPublisher())
	}

	/**
	 * String请求体
	 * @param bodyPublisher Function0<BodyPublisher>
	 * @return HttpHelper<T>
	 */
	fun stringBodyPublisher(body: String): HttpHelper<T> {
		this.bodyPublisher = BodyPublishers.ofString(body)
		return this
	}

	/**
	 * String请求体
	 * @param bodyPublisher Function0<BodyPublisher>
	 * @return HttpHelper<T>
	 */
	inline fun stringBodyPublisher(body: () -> String): HttpHelper<T> {
		return stringBodyPublisher(body())
	}

	/**
	 * url参数
	 * @param params Array<out Pair<String, Any>>
	 * @return HttpGetter<T>
	 */
	fun params(vararg params: Pair<String, Any>): HttpHelper<T> {
		val str = StringBuilder(uri)
		str.append("?")
		params.forEachIndexed { index, pair ->
			str.append(pair.first, "=", pair.second)
			if (index != params.size - 1) str.append("&")
		}
		requestBuilder.uri(URI.create(str.toString()))
		return this
	}

	fun params(params: Map<String, String>): HttpHelper<T> {
		return params(*params.entries.map { it.key to it.value }.toTypedArray())
	}

	/**
	 * 请求头
	 * @param headers Array<out Pair<String, String>>
	 * @return HttpGetter<T>
	 */
	fun headers(vararg headers: Pair<String, String>): HttpHelper<T> {
		headers.onEach {
			requestBuilder.header(it.first, it.second)
		}
		return this
	}

	fun headers(headers: Map<String, String>): HttpHelper<T> {
		return headers(*headers.entries.map { it.key to it.value }.toTypedArray())
	}

	inline fun expectContinue(enable: () -> Boolean): HttpHelper<T> {
		return expectContinue(enable())
	}

	fun expectContinue(enable: Boolean): HttpHelper<T> {
		requestBuilder.expectContinue(enable)
		return this
	}

	fun version(version: Version): HttpHelper<T> {
		requestBuilder.version(version)
		return this
	}

	inline fun version(version: () -> Version): HttpHelper<T> {
		return version(version())
	}


	/**
	 * 发送同步请求
	 * @return HttpResponse<T>
	 */
	suspend fun send(): HttpResponse<T> {
		return withContext(Dispatchers.IO) {
			client.send(requestBuilder.build(), bodyHandler)
		}
	}

	/**
	 * 发送同步请求 返回body
	 * @return T
	 */
	suspend fun sendGetBody(): T {
		return withContext(Dispatchers.IO) {
			client.send(requestBuilder.build(), bodyHandler)
		}.body()
	}

	/**
	 * 发送异步请求
	 * @param action Function1<HttpResponse<T>, Unit>
	 */
	fun sendAsync(action: (HttpResponse<T>) -> Unit) {
		client.sendAsync(requestBuilder.build(), bodyHandler)
			.thenAcceptAsync(action)
	}

	/**
	 * 发送异步请求
	 * @param action Function1<T, Unit> T ：body
	 */
	fun sendAsyncGetBody(action: (T) -> Unit) {
		client.sendAsync(requestBuilder.build(), bodyHandler)
			.thenAcceptAsync { action(it.body()) }
	}
}

