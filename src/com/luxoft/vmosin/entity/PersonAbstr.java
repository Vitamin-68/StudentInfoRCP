package com.luxoft.vmosin.entity;

import org.eclipse.core.runtime.PlatformObject;

public abstract class PersonAbstr extends PlatformObject {

	public abstract String getName();

	public abstract PersonGroup getParent();
}
