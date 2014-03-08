package rtc.timer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.plugin.Plugin;

public class Timer implements Runnable {
	
	public String name;
	public Plugin plugin;
	public boolean stopped;
	
	public List<TimeableHolder> timeables = new LinkedList<TimeableHolder>();
	
	public Timer(String name, Plugin plugin) {
		this.name = name;
		this.plugin = plugin;
	}
	
	public void parse(Object obj) {
		Class<?> lclass = obj.getClass();
		Method[] methods = lclass.getMethods();
		for(Method m : methods) {
			Annotation[] anns = m.getDeclaredAnnotations();
			Timeable found = null;
			for(Annotation ann : anns) {
				if(ann instanceof Timeable) {
					found = (Timeable) ann;
					break;
				}
			}
			
			if(found == null)
				continue;
			
			timeables.add(new TimeableHolder(found, m, obj));
		}
	}
	
	public void tick() {
		for(TimeableHolder th : timeables) {
			try {
				if(th.counter >= th.annotation.period()) {
					th.method.invoke(th.instance, new Object[] {});
					
					th.counter = 0;
				} else {
					th.counter++;
				}
			} catch (IllegalAccessException | IllegalArgumentException| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		if(!stopped)
			tick();
	}
	
	class TimeableHolder {
		Timeable annotation;
		Method method;
		Object instance;
		
		int counter = 0;
		
		public TimeableHolder(Timeable annotation, Method method, Object instance) {
			this.annotation = annotation;
			this.method = method;
			this.instance = instance;
		}
	}
}
