/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusos.funcional;

/**
 *
 * @author christianmendoza
 */

import srv.cuso;

import comun.bkComun;

public class ini extends bkComun {
    public ini(cuso inCuso) throws Exception {
		super(inCuso);
		kComa="'";
	}

	public void lis() throws Exception {
		super.lis();		
	}

	public void graba() throws Exception {
		super.graba();
	}

	public void fin() throws Exception {
		super.fin();
	}

}
