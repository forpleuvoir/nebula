package event;

import moe.forpleuvoir.nebula.event.EventSubscriber;
import moe.forpleuvoir.nebula.event.Subscriber;

@EventSubscriber
public class JavaTestEvent {

	@Subscriber
	public static void t1(TestEvent event) {
		System.out.println(event.getA());
	}

}
