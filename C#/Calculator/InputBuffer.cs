/*
 * Authored by Steve Devoy on November 28, 2023, as an example of my C# programming skills.
 * Copyright (c) 2023, Steve Devoy
 * Total development time: 6 hous
 */

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Diagnostics;

namespace Calculator
{
    public class InputBuffer
    {
        private Tokenizer tokenizer = null;
        private string inputText = "";
        private List<string> inputHistory = new List<string>();

        public InputBuffer (Tokenizer tokenizer)
        {
            this.tokenizer = tokenizer;
        }

        public string Text
        {
            get { return inputText; }
            set {
                if (! string.IsNullOrWhiteSpace(inputText))
                {
                    inputHistory.Insert(0, inputText);
                }
                inputText = value;
            }
        }

        public string Clear()
        {
            inputHistory.Clear();
            tokenizer.Clear();
            return inputText = "";
        }

        public string ClearLastEntry()
        {
            string result = tokenizer.RemoveLastAndReturnString(); // .Replace(" ", String.Empty);

            Debug.WriteLine("\nresult=\"" + result + "\"");

            while (inputHistory.Count > 0)
            {
                string current = inputHistory.First();
                inputHistory.RemoveAt(0);

                Debug.WriteLine("\ncurrent=\"" + current + "\"");

                if (current.Equals(result)) break;
            }

            return inputText = result;
        }

        public string Add(string moreText)
        {
            if (!string.IsNullOrWhiteSpace(inputText))
            {
                inputHistory.Insert(0, inputText);
            }

            // inputText += moreText;

            if (tokenizer != null)
                return inputText = tokenizer.ProcessAndReturnString(inputText + moreText);
            else
                return inputText += moreText;
        }

        public string Undo()
        {
            if (inputHistory.Count > 0)
            {
                inputText = inputHistory[0];
                inputHistory.RemoveAt(0);
            } else
            {
                inputText = "";
            }

            if (tokenizer != null)
                return tokenizer.ProcessAndReturnString(inputText);
            else
                return "";
        }
    }
}
