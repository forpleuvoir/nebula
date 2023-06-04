@file:Suppress("unused")

package moe.forpleuvoir.nebula.common.net

import java.net.http.HttpRequest.BodyPublisher
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandler
import java.net.http.HttpResponse.BodyHandlers


fun <T> httpGet(uri: String, bodyHandler: BodyHandler<T>): HttpHelper<T> {
	return HttpHelper(uri, bodyHandler).apply { requestBuilder.GET() }
}

fun httpGet(uri: String): HttpHelper<String> {
	return httpGet(uri, BodyHandlers.ofString())
}

fun <T> httpDelete(uri: String, bodyHandler: BodyHandler<T>): HttpHelper<T> {
	return HttpHelper(uri, bodyHandler).apply { requestBuilder.DELETE() }
}

fun httpDelete(uri: String): HttpHelper<String> {
	return httpDelete(uri, BodyHandlers.ofString())
}

fun <T> httpPost(uri: String, bodyHandler: BodyHandler<T>, bodyPublisher: BodyPublisher): HttpHelper<T> {
	return HttpHelper(uri, bodyHandler, bodyPublisher).apply { requestBuilder.POST(this.bodyPublisher) }
}

fun httpPost(uri: String, body: String): HttpHelper<String> {
	return httpPost(uri, BodyHandlers.ofString(), BodyPublishers.ofString(body))
}

fun httpPost(uri: String): HttpHelper<String> {
	return httpPost(uri, BodyHandlers.ofString(), BodyPublishers.noBody())
}

fun <T> httpPut(uri: String, bodyHandler: BodyHandler<T>, bodyPublisher: BodyPublisher): HttpHelper<T> {
	return HttpHelper(uri, bodyHandler, bodyPublisher).apply { requestBuilder.PUT(this.bodyPublisher) }
}

fun httpPut(uri: String, body: String): HttpHelper<String> {
	return httpPut(uri, BodyHandlers.ofString(), BodyPublishers.ofString(body))
}

fun httpPut(uri: String): HttpHelper<String> {
	return httpPut(uri, BodyHandlers.ofString(), BodyPublishers.noBody())
}