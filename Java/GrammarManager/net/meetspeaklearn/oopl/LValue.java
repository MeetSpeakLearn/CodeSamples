/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.meetspeaklearn.oopl;

/**
 *
 * @author steve
 */
public interface LValue {
    LValue resolveReference(Scope scope);
}
