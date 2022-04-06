package org.inventivetalent.update.spiget.test;

import org.inventivetalent.update.spiget.comparator.VersionComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComparatorTest {
	@Test
	public void equalsTest() {
		String oldVersion = "1.0.0";
		String newVersion = "2.0.0";

		assertTrue(VersionComparator.EQUAL.isNewer(oldVersion, newVersion));

		// This is a case where the simple equals check doesn't shine.
		assertTrue(VersionComparator.EQUAL.isNewer(newVersion, oldVersion));

		assertFalse(VersionComparator.EQUAL.isNewer(oldVersion, oldVersion));
	}

	@Test
	public void semverTest() {
		String oldVersion = "1.0.0";
		String newVersion = "2.0.0";

		assertTrue(VersionComparator.SEM_VER.isNewer(oldVersion, newVersion));
		assertFalse(VersionComparator.SEM_VER.isNewer(newVersion, oldVersion));
		assertFalse(VersionComparator.SEM_VER.isNewer(oldVersion, oldVersion));
	}

	@Test
	public void longSemverTest() {
		String oldVersion = "1.0";
		String newVersion = "2.0.0.0.0.0";

		assertTrue(VersionComparator.SEM_VER.isNewer(oldVersion, newVersion));
		assertFalse(VersionComparator.SEM_VER.isNewer(newVersion, oldVersion));
		assertFalse(VersionComparator.SEM_VER.isNewer(oldVersion, oldVersion));
	}

	@Test
	public void longSemverTest2() {
		String oldVersion = "5.0.6.2.7";
		String newVersion = "6.0.7.3";

		assertTrue(VersionComparator.SEM_VER.isNewer(oldVersion, newVersion));
		assertFalse(VersionComparator.SEM_VER.isNewer(newVersion, oldVersion));
		assertFalse(VersionComparator.SEM_VER.isNewer(oldVersion, oldVersion));
	}

	@Test
	public void edgeSemverTest() {
		String oldVersion = "1.1.10";
		String newVersion = "1.2.0";

		assertTrue(VersionComparator.SEM_VER.isNewer(oldVersion, newVersion));
		assertFalse(VersionComparator.SEM_VER.isNewer(newVersion, oldVersion));
		assertFalse(VersionComparator.SEM_VER.isNewer(oldVersion, oldVersion));
	}

	@Test
	public void semverSnapshotTest() {
		String oldVersion = "1.0.0-SNAPSHOT";
		String newVersion = "2.0.0-SNAPSHOT";

		assertTrue(VersionComparator.SEM_VER_SNAPSHOT.isNewer(oldVersion, newVersion));
		assertFalse(VersionComparator.SEM_VER_SNAPSHOT.isNewer(newVersion, oldVersion));
		assertFalse(VersionComparator.SEM_VER_SNAPSHOT.isNewer(oldVersion, oldVersion));
	}
}
