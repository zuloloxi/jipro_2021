/*
 * 23/04/2014
 *
 * Copyright (C) 1999-2014 Ugo Chirico - http://www.ugochirico.com
 *
 * This is free software; you can redistribute it and/or
 * modify it under the terms of the Affero GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Affero GNU General Public License for more details.
 *
 * You should have received a copy of the Affero GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.ugos.jiprolog.engine;

//import java.io.Serializable;
import java.util.Hashtable;

final class Expression extends PrologObject //implements Serializable
{
    final static long serialVersionUID = 300000004L;

    private final double m_dValue;
    private boolean floating = false;

    public static Expression createNumber(final double dNum)
    {
        return new Expression(dNum);
    }

    static Expression createDouble(final double dNum)
    {
        Expression exp = new Expression(dNum);
        exp.floating = true;
        return exp;
    }

    public static Expression createNumber(final String strNum)
    {
    	final Expression expr = new Expression(Double.valueOf(strNum).doubleValue());
    	if(strNum.contains("."))
    		expr.floating = true;

    	return expr;
    }

    private Expression(final double dNum)
    {
        m_dValue   = dNum;
        floating = (int)dNum != dNum;
    }

    public final PrologObject copy(final boolean flat, final Hashtable<Variable, PrologObject> varTable)
    {
        return this;
    }

    public final boolean _unify(final PrologObject obj, final Hashtable table)
    {
        //System.out.println("Match Expression");

        if(obj instanceof Expression)
        {
            return ((Expression)obj).floating == floating && (((Expression)obj).m_dValue == m_dValue);
        }
        else if(obj instanceof Variable)
        {
            return obj._unify(this, table);
        }
        else
            return false;
    }

    public final void clear()
    {
    }

    public static final Expression compute(PrologObject exp)
    {
        if(exp instanceof Variable)
            exp = ((Variable)exp).getObject();

        if(exp instanceof Expression)
        {
            return (Expression)exp;
        }
        if(exp instanceof Atom)
        {
            final String strExp = ((Atom)exp).getName();

            if (strExp.equals("rand"))
            {
                return Expression.createNumber(Math.random());
            }
            else if (strExp.equals("e"))
            {
                return Expression.createNumber(Math.E);
            }
            else if (strExp.equals("pi"))
            {
                return Expression.createNumber(Math.PI);
            }
            else if (strExp.equals("cputime"))
            {
                return Expression.createNumber(System.currentTimeMillis());
            }
            else
            {
                throw new JIPTypeException(JIPTypeException.EVALUABLE, new Functor(Atom.createAtom(strExp)).getPredicateIndicator());
            }
        }
        else if(exp instanceof Functor)
        {
            double dblVal;

            try
            {
                final Functor  func       = (Functor) exp;
                final String   strFunName = func.getFriendlyName();
                final ConsCell params     = func.getParams();

//                System.out.println(strFunName);
//                System.out.println(params);
//
                Expression retexp;
                switch(func.getArity())
                {
                    case 1:
                    	double dVal1;

                        if (strFunName.equals("-"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal = - dVal1;

                            retexp = Expression.createNumber(dblVal);
                            retexp.floating = !exp1.isInteger();
                            return retexp;
                        }
                        else if (strFunName.equals("+"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal = dVal1;
                            retexp = Expression.createNumber(dblVal);
                            retexp.floating = !exp1.isInteger();
                            return retexp;

                        }
                        else if (strFunName.equals("sin"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.sin(dVal1);
                        }
                        else if (strFunName.equals("cos"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.cos(dVal1);
                        }
                        else if (strFunName.equals("tan"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.tan(dVal1);
                        }
                        else if (strFunName.equals("asin"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.asin(dVal1);
                        }
                        else if (strFunName.equals("acos"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.acos(dVal1);
                        }
                        else if (strFunName.equals("atan"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.atan(dVal1);
                        }
                        else if (strFunName.equals("log"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.log(dVal1);
                        }
                        else if (strFunName.equals("exp"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.exp(dVal1);
                        }
                        else if (strFunName.equals("int") || strFunName.equals("integer") || strFunName.equals("round"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.rint(dVal1);
                            return Expression.createNumber(dblVal);
                        }
                        else if (strFunName.equals("ceil") || strFunName.equals("ceiling"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.ceil(dVal1);
                            return Expression.createNumber(dblVal);
                        }
                        else if (strFunName.equals("floor") || strFunName.equals("truncate") || strFunName.equals("rnd"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.floor(dVal1);
                            return Expression.createNumber(dblVal);
                        }
                        else if (strFunName.equals("float"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  dVal1;
                        }
                        else if (strFunName.equals("float_fractional_part"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  dVal1 - (int)dVal1;
                        }
                        else if (strFunName.equals("float_integer_part"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  dVal1 - (int)dVal1;
                            return Expression.createNumber(dblVal);
                        }
                        else if (strFunName.equals("abs"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.abs(dVal1);
                            retexp = Expression.createNumber(dblVal);
                            retexp.floating = !exp1.isInteger();
                            return retexp;
                        }
                        else if (strFunName.equals("sqrt"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.sqrt(dVal1);
                        }
                        else if (strFunName.equals("sign"))
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            if(dVal1 > 0)
                                dblVal = 1;
                            else
                                dblVal = -1;

                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("\\")) // bitwise negation
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  ~(int)dVal1;
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("random")) // random
                        {
                        	Expression exp1 = Expression.compute(params.getHead());
                            dVal1 = exp1.m_dValue;

                            dblVal =  Math.random() * dVal1;
                        }
                        else
                        {
//                        	throw new JIPEvaluationException(JIPEvaluationException.undefined);//.create(2, strFunName + " is unknown");
                            throw new JIPTypeException(JIPTypeException.EVALUABLE, new Functor(Atom.createAtom(strFunName + "/1")).getPredicateIndicator());

                        }

                        break;

                    case 2:
                    	PrologObject head = params.getHead();
                        if(head instanceof Variable)
                        	head = ((Variable)head).getObject();

                        if(head == null)
                            throw new JIPTypeException(JIPTypeException.EVALUABLE, new Functor(Atom.createAtom(strFunName + "/2")).getPredicateIndicator());//.create(2, strFunName + " is unknown");



                        if(strFunName.equals("+"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  dVal1 + dVal2;

                            retexp = Expression.createNumber(dblVal);
                            retexp.floating = !exp1.isInteger() || !exp2.isInteger();

                            return retexp;
                        }
                        else if(strFunName.equals("-"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;


                            dblVal =  dVal1 - dVal2;

                            retexp = Expression.createNumber(dblVal);
                            retexp.floating = !exp1.isInteger() || !exp2.isInteger();
                            return retexp;
                        }
                        else if (strFunName.equals("atan2"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;


                        	if(dVal1 == 0 && dVal2 == 0)
                        		throw new JIPEvaluationException(JIPEvaluationException.undefined);

                            dblVal =  Math.atan2(dVal1, dVal2);
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("/"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  dVal1 / dVal2;
                        }
                        else if(strFunName.equals("//"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  ((int)dVal1 / (int)dVal2);
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("*"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  dVal1 * dVal2;
                            retexp = Expression.createNumber(dblVal);
                            retexp.floating = !exp1.isInteger() || !exp2.isInteger();
                            return retexp;
                        }
                        else if (strFunName.equals("pow") || strFunName.equals("**") || strFunName.equals("^"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  Math.pow(dVal1, dVal2);

                            retexp = Expression.createNumber(dblVal);
                            if(dblVal <= Integer.MAX_VALUE)
                            	retexp.floating = !exp1.isInteger() || !exp2.isInteger();
                            return retexp;
                        }
                        else if (strFunName.equals("min"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  Math.min(dVal1, dVal2);

                            return Expression.createNumber(dblVal);
                        }
                        else if (strFunName.equals("max"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  Math.max(dVal1, dVal2);
                            return Expression.createNumber(dblVal);
                        }
                        else if (strFunName.equals("mod") || strFunName.equals("rem"))
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  dVal1 % dVal2;
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("/\\"))  // bitwise and
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  (int)dVal1 & (int)dVal2;
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("\\/")) // bitwise or
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  (int)dVal1 | (int)dVal2;
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("<<")) // left shift
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  (int)dVal1 << (int)dVal2;
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("xor")) // xor
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  (int)dVal1 ^ (int)dVal2;
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals(">>")) // right shift
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  (int)dVal1 >> (int)dVal2;
                            return Expression.createNumber(dblVal);
                        }
                        else if(strFunName.equals("div")) // DIV
                        {
                        	final Expression exp1 = Expression.compute(head);
                            dVal1 = exp1.m_dValue;

                            final Expression exp2 = Expression.compute(((ConsCell)params.getTail()).getHead());
                            final double dVal2 = exp2.m_dValue;

                            dblVal =  (int)(dVal1 - dVal1 % dVal2) / dVal2;
                        }
                        else
                        {
                            throw new JIPTypeException(JIPTypeException.EVALUABLE, new Functor(Atom.createAtom(strFunName + "/2")).getPredicateIndicator());//.create(2, strFunName + " is unknown");
                        }
                        break;

                    default:
                        throw new JIPTypeException(JIPTypeException.EVALUABLE, new Functor(Atom.createAtom(strFunName + "/2")).getPredicateIndicator());//.create(2, strFunName + " is unknown");
                }

                return Expression.createDouble(dblVal);
            }
            catch(JIPRuntimeException ex)
            {
                throw ex;
            }
            catch(ClassCastException ex)
            {
            	throw new JIPTypeException(JIPTypeException.EVALUABLE, exp);
            }
            catch(NullPointerException ex)
            {
            	throw new JIPEvaluationException(JIPEvaluationException.undefined);
//                throw JIPRuntimeException.create(2, "Wrong number of arguments in expression");
            }
        }
        else if(exp instanceof ConsCell && !(exp instanceof List))
        {
            if(((ConsCell)exp).getHeight() != 1)
            	throw new JIPTypeException(JIPTypeException.EVALUABLE, exp);

            return compute(((ConsCell)exp).getHead());
        }
        else if (exp == null)
        {
            throw new JIPParameterUnboundedException();
        }
        else
        {
        	throw new JIPTypeException(JIPTypeException.EVALUABLE, exp);
        }
    }

    public final double getValue()
    {
        //System.out.println(m_Operator);
        return m_dValue;
    }

    protected final boolean lessThen(final PrologObject obj)
    {
        if(obj instanceof Expression)
        {
        	if(floating && !((Expression)obj).floating)
        		return true;
        	else if(((Expression)obj).floating && !floating)
        		return false;
        	else if(m_dValue < ((Expression)obj).m_dValue)
            	return true;
        	else
        		return false;
        }
        else if(obj instanceof Atom)
        {
        	return true;
        }
        else if(obj instanceof Variable)
        {
            if(((Variable)obj).isBounded())
            {
                if(obj.unifiable(List.NIL))
                	return true;
                else
                	return lessThen(((Variable)obj).getObject());
            }
        }

        return true;
    }

    public final boolean isInteger()
    {
        return !floating;//(int)m_dValue == m_dValue;
    }

    @Override
    public boolean termEquals(PrologObject obj)
    {
        if(obj instanceof Expression)
        {
            return m_dValue == ((Expression)obj).m_dValue && floating == ((Expression)obj).floating;
        }
        else if(obj instanceof Variable && ((Variable)obj).isBounded())
            return termEquals(((Variable)obj).getObject());

        return false;
    }
}
