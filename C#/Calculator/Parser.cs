/*
 * Authored by Steve Devoy on November 28, 2023, as an example of my C# programming skills.
 * Copyright (c) 2023, Steve Devoy
 * Total development time: 6 hous
 */

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Calculator
{
    public class ParserNode
    {
        private Token _token = null;
        private List<Token> _tokensToTheLeft = new List<Token>();
        private List<Token> _tokensToTheRight = new List<Token>();
        private List<ParserNode> _parameters = new List<ParserNode>();
        private bool _parsed = false;
        private string _errorMessage = null;
        private Func<double, double, double> _eval;

        public double Eval()
        {
            if (_eval != null)
            {
                switch (_parameters.Count)
                {
                    case 0: return _eval(0.0, 0.0);
                    case 1: return _eval(_parameters[0].Eval(), 0.0);
                    case 2: return _eval(_parameters[0].Eval(), _parameters[1].Eval());
                    default: return 0.0;
                }
            }
            else if (_parameters.Count > 0)
                return _parameters[0].Eval();
            else
                return 0.0;
        }

        public double ProtectedEval()
        {
            try
            {
                return Eval();
            } catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return 0.0;
            }
        }

        public string ProtectedEvalAsText()
        {
            double result = ProtectedEval();

            return result.ToString();
        }

        public override string ToString()
        {
            string token = (_token == null) ? "null" : _token.Name;
            string parameters = null;

            if (_parameters == null)
                parameters = "";
            else
            {
                bool firstPass = true;
                foreach(ParserNode node in _parameters)
                {
                    if (!firstPass)
                        parameters += " ";
                    else
                        firstPass = false;

                    parameters += node;
                }
            }

            return "[" + token + " (" + parameters + ")]";
        }

        public ParserNode(List<Token> inputTokens)
        {
            Console.WriteLine("ParserNode(" + inputTokens + ")");
            Token weakestOperator = null;
            List<ParserNode> nodes = new List<ParserNode>();
            int parenDepth = 0;

            foreach (var token in inputTokens)
            {
                if (parenDepth == 0)
                {
                    if (token.Type == TokenType.OpenParen)
                    {
                        parenDepth++;
                    }
                    else if (token.Type == TokenType.CloseParen)
                    {
                        parenDepth--;
                        //if (parenDepth < 0)
                        //{
                        //    _parsed = false;
                        //    _errorMessage = "Right parenthesis ecnountered without opening left prenethesis.";
                        //    return;
                        //}
                    }
                    else if (token.Priority != int.MinValue)
                    {
                        if (weakestOperator == null)
                            weakestOperator = token;
                        else if (token.Priority > weakestOperator.Priority)
                            weakestOperator = token;
                    }
                } else
                {
                    if (token.Type == TokenType.OpenParen)
                    {
                        parenDepth++;
                    }
                    else if (token.Type == TokenType.CloseParen)
                    {
                        parenDepth--;
                    }
                }
            }

            parenDepth = 0;

            if (weakestOperator != null)
            {
                Console.WriteLine("Weakest operator is: " + weakestOperator.Name);
                bool passedWeakestOperator = false;

                foreach (var token in inputTokens)
                {
                    if (parenDepth == 0)
                    {
                        if (token == weakestOperator)
                        {
                            passedWeakestOperator = true;
                            Token = token;
                        }
                        else if (token.Type == TokenType.OpenParen)
                        {
                            parenDepth += 1;
                        }
                        else if (token.Type == TokenType.CloseParen)
                        {
                            parenDepth -= 1;
                            //if (parenDepth < 0)
                            //{
                            //    _parsed = false;
                            //    return;
                            //}
                        }
                        else if (passedWeakestOperator)
                        {
                            Console.WriteLine("adding token " + token.Name + " to the right");
                            _tokensToTheRight.Add(token);
                        }
                        else
                        {
                            Console.WriteLine("adding token " + token.Name + " to the left");
                            _tokensToTheLeft.Add(token);
                        }
                    }
                    else
                    {
                        if (token.Type == TokenType.OpenParen)
                        {
                            parenDepth += 1;
                        }
                        else if (token.Type == TokenType.CloseParen)
                        {
                            parenDepth -= 1;
                            //if (parenDepth < 0)
                            //{
                            //    _parsed = false;
                            //    return;
                            //}
                        }

                        if (passedWeakestOperator)
                        {
                            Console.WriteLine("adding token " + token.Name + " to the right");
                            _tokensToTheRight.Add(token);

                        }
                        else
                        {
                            Console.WriteLine("adding token " + token.Name + " to the left");
                            _tokensToTheLeft.Add(token);
                        }
                    }
                }

                if (_tokensToTheLeft.Count > 0) _parameters.Add(new ParserNode(_tokensToTheLeft));
                if (_tokensToTheRight.Count > 0) _parameters.Add(new ParserNode(_tokensToTheRight));
            }
            else
            {
                List<Token> parenthesizedTokens = null;

                foreach (var token in inputTokens)
                {
                    if (parenDepth == 0)
                    {
                        if (token.Type == TokenType.OpenParen)
                        {
                            parenDepth += 1;
                            parenthesizedTokens = new List<Token>();
                        } else if (token.Type == TokenType.CloseParen)
                        {
                            _errorMessage = "Right parenthesis ecnountered without opening left prenethesis.";
                            return;
                        } else if ((token.Type == TokenType.Number) || (token.Type == TokenType.Variable) || (token.Type == TokenType.Constant))
                        {
                            if (_token == null)
                                Token = token;
                            else
                            {
                                _errorMessage = "Two operands without connecting operator detected.";
                                return;
                            }
                        }
                    } else
                    {
                        if (token.Type == TokenType.CloseParen)
                        {
                            parenDepth -= 1;

                            if (parenDepth == 0)
                            {
                                _parameters.Add(new ParserNode(parenthesizedTokens));
                                parenthesizedTokens = null;
                            } else
                            {
                                parenthesizedTokens.Add(token);
                            }
                        } else if (token.Type == TokenType.OpenParen)
                        {
                            parenDepth += 1;
                            parenthesizedTokens.Add(token);
                        } else
                        {
                            parenthesizedTokens.Add(token);
                        }
                    }
                }
            }
        }

        public Token Token {
            get { return _token; }
            set {
                _token = value;

                if (value != null)
                {
                    switch (value.Type)
                    {
                        case TokenType.BinaryInfixOperator:
                            if (value.Name.Equals("+"))
                                _eval = (op1, op2) => { return op1 + op2; };
                            else if (value.Name.Equals("-"))
                                _eval = (op1, op2) => { return op1 - op2; };
                            else if (value.Name.Equals("x"))
                                _eval = (op1, op2) => { return op1 * op2; };
                            else if (value.Name.Equals("÷"))
                                _eval = (op1, op2) => { return op1 / op2; };
                            else if (value.Name.Equals("^"))
                                _eval = (op1, op2) => { return Math.Pow(op1, op2); };
                            break;
                        case TokenType.UnaryPrefixOperator:
                            if (value.Name.Equals("-"))
                                _eval = (op1, op2) => { return - op1; }; // Second argument ignored.
                            else if (value.Name.Equals("√"))
                                _eval = (op1, op2) => { return Math.Sqrt(op1); }; // Second argument ignored.
                            break;
                        case TokenType.UnaryPostfixOperator:
                            if (value.Name.Equals("%"))
                                _eval = (op1, op2) => { return op1 / 100.0; }; // Second argument ignored.
                            break;
                        case TokenType.Number:
                            _eval = (op1, op2) => { return Double.Parse(value.Name); }; // Both arguments ignored.
                            break;
                        case TokenType.Constant:
                            if (value.Name.Equals("π"))
                                _eval = (op1, op2) => { return Math.PI; }; // Both arguments ignored.
                            if (value.Name.Equals("e"))
                                _eval = (op1, op2) => { return Math.E; }; // Both arguments ignored.
                            break;

                    }
                } else
                {
                    _eval = (op1, op2) => { return op1; }; // Second argument ignored.
                }
            }
        }
    }

    public class Parser
    {
        private Form1 _interface = null;
        private ParserNode _root = null;

        public Parser() { }
        public Parser(Form1 ui)
        {
            _interface = ui;
        }

        public ParserNode Parse(List<Token> tokens)
        {
            _root = new ParserNode(tokens);

            Console.WriteLine("Parse Tree: \n" + _root);

            return _root;
        }

        public double ProtectedEval()
        {
            double result;

            try
            {
                if (_root != null)
                {
                    result = _root.Eval();
                    _interface.ResultText = result.ToString();
                    return result;
                }
                else
                    return 0.0;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                if (_interface != null) _interface.ResultText = ex.Message;
                return 0.0;
            }
        }

        public string ProtectedEvalAsText()
        {
            double result = ProtectedEval();

            return result.ToString();
        }
    }
}
