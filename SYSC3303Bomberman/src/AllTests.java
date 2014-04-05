

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ConcurrencyTestCase.class, FunctionalTestCase.class, ScalabilityTestCase.class })
public class AllTests {

}
