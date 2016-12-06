import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class QueueTest
{
	public static void main(String[] args)
	{
		QueueTest test=new QueueTest();
		ArrayBlockingQueue queue=new ArrayBlockingQueue<String>(1000);
		 
		Producer p1=test.new Producer(queue,"producer1");
		Producer p2=test.new Producer(queue,"producer2");
		
		//new Thread(p1).start();
		Consumer c1=test.new Consumer(queue,"consumer1");
		Consumer c2=test.new Consumer(queue,"consumer2");
		//new Thread(c1).start();
		//new Thread(c1).start();

		//new Thread(c1).start();
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.execute(p1);
		executor.execute(p2);
		executor.execute(c1);
		executor.execute(c2);
		
		
	}
	public class Producer implements Runnable
	{
		private BlockingQueue queue;
		private String name;
		private int i=0;
		public Producer(ArrayBlockingQueue queue,String name)
		{
			this.queue=queue;
			this.name=name;
		}
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			while(true)
			{
				i++;
				queue.add(name+"  "+i);
				System.out.println("add producer"+i);
				if(i>100)
					return;
				try
				{
					Thread.sleep(10);
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public class Consumer implements Runnable
	{
		private BlockingQueue queue;
		private String name;
		public Consumer(ArrayBlockingQueue queue,String name)
		{
			this.queue=queue;
			this.name=name;
		}
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			while(true)
			{
				try
				{
					System.out.println("thread "+name+queue.take().toString());
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
//	public class Producer extends Thread
//	{
//		private int i;
//		private String threadName;
//		public Producer(String threadName,int i)
//		{
//			this.threadName=threadName;
//			this.i=i;
//		}
//		@Override
//		public void run()
//		{
//			// TODO Auto-generated method stub
//			super.run();
//			for(int a=0;a<i;a++)
//			{
//				if(a>10)
//				{
//					return;
//				}
//				System.out.println("this is "+threadName+"  "+a+"ÔËÐÐ");
//				try
//				{
//					Thread.sleep(1000);
//				}
//				catch (InterruptedException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}
		
		
		
	//}
}


