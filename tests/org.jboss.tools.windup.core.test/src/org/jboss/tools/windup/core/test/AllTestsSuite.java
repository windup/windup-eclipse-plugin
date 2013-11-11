package org.jboss.tools.windup.core.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	WindupValidatorTest.class,
	WindupServiceTest.class
})
public class AllTestsSuite {

}