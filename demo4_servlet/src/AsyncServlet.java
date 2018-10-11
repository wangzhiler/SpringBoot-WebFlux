import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@WebServlet(urlPatterns = {"/AsyncServlet"}, asyncSupported = true)
public class AsyncServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long t1 = System.currentTimeMillis();

        //开启异步
        AsyncContext asyncContext = request.startAsync();

        //执行业务代码,从request获取信息，返回给response
        //异步操作可以把耗时放入线程池
        //方便起见
        CompletableFuture.runAsync(() ->
        //异步处理是在另一个线程里，处理完要主动告诉asynContext自己结束了，所以也要传入
                doSomeThing(asyncContext,asyncContext.getRequest(), asyncContext.getResponse()));


        System.out.println("耗时：" + (System.currentTimeMillis() - t1));
    }

    private void doSomeThing(AsyncContext asyncContext, ServletRequest request, ServletResponse response){
        try {
            //模拟耗时操作
            TimeUnit.SECONDS.sleep(5);
            response.getWriter().append("done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //业务代码处理完毕，通知结束
        asyncContext.complete();

    }
}
