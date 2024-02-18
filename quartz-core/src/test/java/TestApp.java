import net.c0ffee1.quartz.core.QuartzApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestApp implements QuartzApplication {
    @Override
    public Logger getSLF4JLogger() {
        return LoggerFactory.getLogger("test");
    }
}
