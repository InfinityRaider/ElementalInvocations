/*
 */
package com.infinityraider.elementalinvocations.utility;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class AreaHelper {
	
	public static AxisAlignedBB getArea(Vec3d center, double radius) {
		return getArea(center, radius, radius, radius);
	}
	
	public static AxisAlignedBB getArea(Vec3d center, double xrad, double yrad, double zrad) {
		return new AxisAlignedBB(
				center.xCoord - xrad,
				center.yCoord - yrad,
				center.zCoord - zrad,
				center.xCoord + xrad,
				center.yCoord + yrad,
				center.zCoord + zrad
		);
	}
	
}
