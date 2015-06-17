package com.github.easypack.platform;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.easypack.platform.Platform;
import com.github.easypack.platform.PlatformBehavioural;

/**
 * Unit test for {@link Platform}.
 * 
 * @author agusmunioz
 * 
 */
public class PlatformTest {

	/**
	 * Test the conversion of a valid list of comma separated platforms.
	 */
	@Test
	public void commaSeparated() {

		Collection<Platform> platforms = Platform.fromString("windows, linux");

		Assert.assertEquals("Unexpected amount of platforms", 2,
				platforms.size());

		Assert.assertTrue("Windows not included.",
				platforms.contains(Platform.WINDOWS));

		Assert.assertTrue("Linux not included",
				platforms.contains(Platform.LINUX));

	}

	/**
	 * Test the conversion of a list of comma separated platforms with an
	 * invalid platform.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void commaSeparatedWithInvalid() {

		Platform.fromString("windows, linux, invalid");

		Assert.fail("A list of platform including an invalid one"
				+ " was correctly converted");
	}

	/**
	 * Test the valid conversion of a platform from a String.
	 */
	@Test
	public void onePlatform() {

		Collection<Platform> platforms = Platform.fromString("windows");

		Assert.assertEquals("Unexpected amount of platforms", 1,
				platforms.size());

		Assert.assertTrue("Windows not included.",
				platforms.contains(Platform.WINDOWS));

	}

	/**
	 * Test an exception is thrown when an invalid platform is converted.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidPlatform() {

		Platform.fromString("invalid");

		Assert.fail("An invalid platform was correctly converted");
	}

	/**
	 * Test {@link Platform#LINUX} triggers the correct behavior.
	 */
	@Test
	public void linuxBehavioural() {

		@SuppressWarnings("unchecked")
		PlatformBehavioural<Void> behavioural = Mockito
				.mock(PlatformBehavioural.class);

		Platform.LINUX.behave(behavioural);

		Mockito.verify(behavioural, Mockito.timeout(1)).linux();

	}

	/**
	 * Test {@link Platform#WINDOWS} triggers the correct behavior.
	 */
	@Test
	public void windowsBehavioural() {

		@SuppressWarnings("unchecked")
		PlatformBehavioural<Void> behavioural = Mockito
				.mock(PlatformBehavioural.class);

		Platform.WINDOWS.behave(behavioural);

		Mockito.verify(behavioural, Mockito.timeout(1)).windows();

	}
}
