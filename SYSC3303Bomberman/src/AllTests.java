

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FunctionalTestCase.class, ScalabilityTestCase.class, ConcurrencyTestCase.class  })
public class AllTests {

}
