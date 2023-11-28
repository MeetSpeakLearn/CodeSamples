/*
 * Authored by Steve Devoy on November 28, 2023, as an example of my C# programming skills.
 * Copyright (c) 2023, Steve Devoy
 * Total development time: 6 hous
 */

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Calculator
{
    public partial class Form1 : Form
    {
        Tokenizer tokenizer = null;
        InputBuffer inputBuffer = null;
        Parser parser = null;

        public Form1()
        {
            InitializeComponent();
            tokenizer = new Tokenizer();
            inputBuffer = new InputBuffer(tokenizer);
            parser = new Parser(this);
        }

        public string Error
        {
            set
            {
                this.errorLabel.Text = value;
            }
        }

        public string ResultText { set { this.resultLabel.Text = value; } }

        private void updateInputBox(string newText)
        {
            this.inputBox.Text = newText;
        }

        private void oneButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("1"));
        }

        private void zeroButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("0"));
        }

        private void twoButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("2"));
        }

        private void threeButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("3"));
        }

        private void fourButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("4"));
        }

        private void fiveButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("5"));
        }

        private void sixButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("6"));
        }

        private void sevenButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("7"));
        }

        private void eightButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("8"));
        }

        private void nineButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("9"));
        }

        private void dotButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("."));
        }

        private void plusButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("+"));
        }

        private void minusButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("—"));
        }

        private void timesButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("x"));
        }

        private void divisionButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("÷"));
        }

        private void backupButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Undo());
        }

        private void toggleSignButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("-"));
        }

        private void leftParenButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("("));

        }

        private void rightParenButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add(")"));
        }

        private void clearAllButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Clear());
            this.Error = "";
            this.ResultText = "";
        }

        private void clearEntryButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.ClearLastEntry());
            resultLabel.Text = string.Empty;
        }

        private void squareRoot_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("√"));
        }

        private void squareButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("^"));
        }

        private void equalsButton_Click(object sender, EventArgs e)
        {
            try
            {
                tokenizer.Validate(tokenizer.Tokens);
            } catch (Exception ex)
            {
                this.Error = ex.Message;
                return;
            }

            ParserNode parseTree = parser.Parse(tokenizer.Tokens);
            if (parseTree != null)
            {
                Console.WriteLine("Parse Tree: \n" + parseTree);

                string resultAsText = parseTree.ProtectedEvalAsText();
                this.resultLabel.Text = resultAsText;
            }
        }

        private void percentButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("%"));
        }

        private void piButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("π"));
        }

        private void eButton_Click(object sender, EventArgs e)
        {
            updateInputBox(inputBuffer.Add("e"));
        }

        private void inverseButton_Click(object sender, EventArgs e)
        {

        }
    }
}
