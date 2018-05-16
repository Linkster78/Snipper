package com.tek.snip.jna;

import com.sun.jna.Library;

public interface User32 extends Library{
	
	short GetKeyState(int nVirtKey);
	
}
