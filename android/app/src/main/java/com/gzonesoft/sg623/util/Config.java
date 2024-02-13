package com.gzonesoft.sg623.util;

public final class Config {

	//public static final BuildType BUILD_TYPE = BuildType.RELEASE;
	public static final BuildType BUILD_TYPE = Svc.buildType;
	
	protected static enum BuildType {
		DEBUG, RELEASE
	}
	
}
