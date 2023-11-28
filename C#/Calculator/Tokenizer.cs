/*
 * Authored by Steve Devoy on November 28, 2023, as an example of my C# programming skills.
 * Copyright (c) 2023, Steve Devoy
 * Total development time: 6 hous
 */

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing.Text;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Calculator
{
    public enum TokenType
    {
        UnaryPrefixOperator,
        UnaryPostfixOperator,
        BinaryInfixOperator,
        Number,
        Variable,
        OpenParen,
        CloseParen,
        Constant,
        Unknown
    }

    public class Token
    {
        private static Regex numberPattern = new Regex(@"^?[0-9][0-9,\.]+$");
        private static Regex variablePattern = new Regex(@"^[x-z]+$");
        private static string orderPrecedence = "-√%^x÷+—";

        private TokenType _type;
        private int _priority;
        private string _name;

        public TokenType Type { get { return _type; } }
        public int Priority { get { return _priority; } }
        public string Name { get { return _name; } }

        public override string ToString()
        {
            return (_name == String.Empty) ? "nameless" : _name;
        }

        protected int LookUpPriority(string op)
        {
            int priority = orderPrecedence.IndexOf(op);

            if (priority < 0)
            {
                return int.MinValue;
            }
            else
                return priority;
        }

        public Token(string text) {
            bool isOperator = false; // Consider deleting this line
            Int64 int64;
            double real;

            if (text.Equals("("))
            {
                _type = TokenType.OpenParen;
            } else if (text.Equals(")"))
            {
                _type = TokenType.CloseParen;
            }
            else if (text.Equals("+"))
            {
                _type = TokenType.BinaryInfixOperator;
                isOperator = true;
            }
            else if (text.Equals("—"))
            {
                _type = TokenType.BinaryInfixOperator;
                isOperator = true;
            }
            else if (text.Equals("-"))
            {
                _type = TokenType.UnaryPrefixOperator;
                isOperator = true;
            }
            else if (text.Equals("x"))
            {
                _type = TokenType.BinaryInfixOperator;
                isOperator = true;
            }
            else if (text.Equals("÷"))
            {
                _type = TokenType.BinaryInfixOperator;
                isOperator = true;
            }
            else if (text.Equals("^"))
            {
                _type = TokenType.BinaryInfixOperator;
                isOperator = true;
            }
            else if (text.Equals("%"))
            {
                _type = TokenType.UnaryPostfixOperator;
                isOperator = true;
            }
            else if (text.Equals("√"))
            {
                _type = TokenType.UnaryPrefixOperator;
                isOperator = true;
            }
            else if (text.Equals("π"))
            {
                _type = TokenType.Constant;
            }
            else if (text.Equals("e"))
            {
                _type = TokenType.Constant;
            }
            else if (Int64.TryParse(text, out int64) || double.TryParse(text, out real))
            {
                _type = TokenType.Number;
            }
            else if (variablePattern.IsMatch(text))
            {
                _type = TokenType.Variable;
            } else
            {
                _type = TokenType.Unknown;
            }

            _priority = LookUpPriority(text);

            _name = text;
        }
    }

    public class Tokenizer
    {
        private static Regex regex = new Regex("-");

        private List<Token> _tokens = null;
        public List<Token> Tokens { get { return _tokens; } }

        public Tokenizer()
        {
            _tokens = new List<Token>();
        }

        public void Validate(List<Token> tokens)
        {
            int parenCount = 0;
            Token previous = null;

            foreach (Token token in tokens)
            {
                if (token.Type == TokenType.OpenParen) parenCount++;
                else if (token.Type == TokenType.CloseParen) parenCount--;

                if (parenCount < 0)
                    throw new Exception("Too many closing parentheses.");

                if (previous == null)
                {
                    if ((token.Type == TokenType.BinaryInfixOperator) || (token.Type == TokenType.UnaryPostfixOperator))
                        throw new Exception("The " + token.Name + " has no left operand.");
                    else if (token.Type == TokenType.CloseParen)
                        throw new Exception("Initial close parenthesis has no matching open parenthesis.");
                }
                else if (token.Type == TokenType.BinaryInfixOperator)
                {
                    if ((previous.Type == TokenType.BinaryInfixOperator) || (previous.Type == TokenType.UnaryPrefixOperator))
                        throw new Exception("Operator " + previous.Name + " cannot immediately precede operator " + token.Name + ".");
                    if (previous.Type == TokenType.OpenParen)
                        throw new Exception("An open parenthesis cannot immediately precede the operator " + token.Name + ".");
                } else if (token.Type == TokenType.UnaryPostfixOperator)
                {
                    if ((previous.Type == TokenType.BinaryInfixOperator) || (token.Type == TokenType.UnaryPrefixOperator))
                        throw new Exception("Operator " + previous.Name + " cannot immediately precede operator " + token.Name + ".");
                    if (previous.Type == TokenType.OpenParen)
                        throw new Exception("An open parenthesis cannot immediately precede the operator " + token.Name + ".");
                } else if ((token.Type == TokenType.Number)
                            || (token.Type == TokenType.Variable)
                            || (token.Type == TokenType.Constant))
                {
                    if ((previous.Type == TokenType.Number)
                            || (previous.Type == TokenType.Variable)
                            || (previous.Type == TokenType.Constant))
                        throw new Exception("Two values without an operator: " + previous.Name + " and " + token.Name + ".");
                    if (previous.Type == TokenType.UnaryPostfixOperator)
                        throw new Exception("The postfix operator " + previous.Name + " cannot precede a value.");
                    if (previous.Type == TokenType.CloseParen)
                        throw new Exception("The value " + token.Name + " is preceded by a value.");
                }

                    previous = token;
            }

            if ((previous.Type == TokenType.BinaryInfixOperator)
                || (previous.Type == TokenType.UnaryPrefixOperator))
                throw new Exception("Operator " + previous.Name + " does not have a right operand.");

            if (parenCount > 0)
                throw new Exception("Too few closing parentheses.");
        }

        public void Clear()
        {
            _tokens.Clear();
        }

        public Token GetLast()
        {
            return _tokens.ElementAt(_tokens.Count - 1);
        }

        public string GetLastAsString()
        {
            Token last = GetLast();

            if (last == null)
                return "";
            else
                return last.ToString();
        }

        public List<Token> RemoveLast()
        {
            if (_tokens != null)
                if (_tokens.Count > 0)
                _tokens.RemoveAt(_tokens.Count - 1);
            return _tokens;
        }

        public string RemoveLastAndReturnString()
        {
            return TokenListToString(RemoveLast());
        }

        public List<Token> Process(string input)
        {
            string[] arrayOfElements;
            string filteredString = "";
            bool firstIteration = true;
            bool previousWasOperator = false;

            // Remove all whitespace

            input = input.Replace(" ", String.Empty);

            // Introduce spaces around operators.

            foreach (char c in input)
            {
                switch (c)
                {
                    // Operators
                    case '(':
                    case ')':
                    case '+':
                    case '—': // Subtraction
                    case '-': // Negation
                    case 'x':
                    case '÷':
                    case '%':
                    case '√':
                    case '^':
                        if (!firstIteration)
                            filteredString += ' ';
                        else
                            firstIteration = false;

                        filteredString += c;
                        previousWasOperator = true;

                        //if ((! firstIteration) && (c != ')')) filteredString += ' ';
                        //filteredString += c;
                        //if (c != '(') filteredString += ' ';
                        break;
                    // Constants
                    case 'π':
                    case 'e':
                        if (!firstIteration)
                            filteredString += ' ';
                        else
                            firstIteration = false;

                        filteredString += c;
                        previousWasOperator = true;
                        break;
                    default:
                        if (previousWasOperator)
                        {
                            filteredString += ' ';
                            previousWasOperator = false;
                        }
                        filteredString += c;
                        break;
                }
                firstIteration = false;
            }

            arrayOfElements = filteredString.Split(' ');

            List<Token> result = new List<Token>();

            foreach (string tokenText in arrayOfElements)
            {
                result.Add(new Token(tokenText));
            }

            return _tokens = result;
        }

        public string TokenListToString(List<Token> tokens)
        {
            string result = "";
            Token previousToken = null;

            foreach (Token token in tokens)
            {
                bool precedingSpace = true;

                if (previousToken == null)
                    precedingSpace = false;
                else
                    switch (previousToken.Type)
                    {
                        case TokenType.UnaryPrefixOperator: precedingSpace = false; break;
                        case TokenType.Number:
                        case TokenType.Variable:
                            switch (token.Type)
                            {
                                case TokenType.CloseParen: precedingSpace = false; break;
                                case TokenType.UnaryPostfixOperator: precedingSpace = false; break;

                            }
                            break;
                        case TokenType.OpenParen: precedingSpace = false; break;
                        case TokenType.CloseParen:
                            switch (token.Type)
                            {
                                case TokenType.CloseParen: precedingSpace = false; break;
                                case TokenType.UnaryPostfixOperator: precedingSpace = false; break;
                            }
                            break;

                    }

                if (precedingSpace) result += " ";
                result += token.Name;
                previousToken = token;
            }

            return result;
        }

        public string ProcessAndReturnString(string input)
        {
            return TokenListToString(Process(input));
        }
    }
}
