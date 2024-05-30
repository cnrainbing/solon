package labs.test1;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.noear.solon.test.SolonJUnit5Extension;
import org.noear.solon.test.SolonTest;

/**
 * @author noear 2021/5/10 created
 */
@SolonTest
@Slf4j
public class LogTest2 {
    @Test
    public void test(){
        log.trace("test");
    }
}
