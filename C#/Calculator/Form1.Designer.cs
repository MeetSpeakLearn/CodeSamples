namespace Calculator
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.inputBox = new System.Windows.Forms.TextBox();
            this.resultLabel = new System.Windows.Forms.Label();
            this.toggleSignButton = new System.Windows.Forms.Button();
            this.zeroButton = new System.Windows.Forms.Button();
            this.oneButton = new System.Windows.Forms.Button();
            this.dotButton = new System.Windows.Forms.Button();
            this.equalsButton = new System.Windows.Forms.Button();
            this.twoButton = new System.Windows.Forms.Button();
            this.threeButton = new System.Windows.Forms.Button();
            this.plusButton = new System.Windows.Forms.Button();
            this.minusButton = new System.Windows.Forms.Button();
            this.sixButton = new System.Windows.Forms.Button();
            this.fiveButton = new System.Windows.Forms.Button();
            this.fourButton = new System.Windows.Forms.Button();
            this.timesButton = new System.Windows.Forms.Button();
            this.nineButton = new System.Windows.Forms.Button();
            this.eightButton = new System.Windows.Forms.Button();
            this.sevenButton = new System.Windows.Forms.Button();
            this.divisionButton = new System.Windows.Forms.Button();
            this.squareRoot = new System.Windows.Forms.Button();
            this.squareButton = new System.Windows.Forms.Button();
            this.backupButton = new System.Windows.Forms.Button();
            this.clearAllButton = new System.Windows.Forms.Button();
            this.clearEntryButton = new System.Windows.Forms.Button();
            this.percentButton = new System.Windows.Forms.Button();
            this.rightParenButton = new System.Windows.Forms.Button();
            this.leftParenButton = new System.Windows.Forms.Button();
            this.piButton = new System.Windows.Forms.Button();
            this.eButton = new System.Windows.Forms.Button();
            this.errorLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // inputBox
            // 
            this.inputBox.BackColor = System.Drawing.SystemColors.ControlDark;
            this.inputBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 14F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.inputBox.Location = new System.Drawing.Point(0, 0);
            this.inputBox.Name = "inputBox";
            this.inputBox.Size = new System.Drawing.Size(528, 34);
            this.inputBox.TabIndex = 0;
            this.inputBox.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
            // 
            // resultLabel
            // 
            this.resultLabel.AutoSize = true;
            this.resultLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 36F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.resultLabel.Location = new System.Drawing.Point(58, 37);
            this.resultLabel.MinimumSize = new System.Drawing.Size(440, 0);
            this.resultLabel.Name = "resultLabel";
            this.resultLabel.Size = new System.Drawing.Size(440, 69);
            this.resultLabel.TabIndex = 1;
            this.resultLabel.Text = "0";
            this.resultLabel.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // toggleSignButton
            // 
            this.toggleSignButton.BackColor = System.Drawing.Color.DarkSlateBlue;
            this.toggleSignButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.toggleSignButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.toggleSignButton.Location = new System.Drawing.Point(0, 560);
            this.toggleSignButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.toggleSignButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.toggleSignButton.Name = "toggleSignButton";
            this.toggleSignButton.Size = new System.Drawing.Size(120, 60);
            this.toggleSignButton.TabIndex = 2;
            this.toggleSignButton.Text = "Neg";
            this.toggleSignButton.UseVisualStyleBackColor = false;
            this.toggleSignButton.Click += new System.EventHandler(this.toggleSignButton_Click);
            // 
            // zeroButton
            // 
            this.zeroButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.zeroButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.zeroButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.zeroButton.Location = new System.Drawing.Point(126, 560);
            this.zeroButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.zeroButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.zeroButton.Name = "zeroButton";
            this.zeroButton.Size = new System.Drawing.Size(120, 60);
            this.zeroButton.TabIndex = 3;
            this.zeroButton.Text = "0";
            this.zeroButton.UseVisualStyleBackColor = false;
            this.zeroButton.Click += new System.EventHandler(this.zeroButton_Click);
            // 
            // oneButton
            // 
            this.oneButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.oneButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.oneButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.oneButton.Location = new System.Drawing.Point(0, 494);
            this.oneButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.oneButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.oneButton.Name = "oneButton";
            this.oneButton.Size = new System.Drawing.Size(120, 60);
            this.oneButton.TabIndex = 4;
            this.oneButton.Text = "1";
            this.oneButton.UseVisualStyleBackColor = false;
            this.oneButton.Click += new System.EventHandler(this.oneButton_Click);
            // 
            // dotButton
            // 
            this.dotButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.dotButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.dotButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.dotButton.Location = new System.Drawing.Point(252, 560);
            this.dotButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.dotButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.dotButton.Name = "dotButton";
            this.dotButton.Size = new System.Drawing.Size(120, 60);
            this.dotButton.TabIndex = 5;
            this.dotButton.Text = ".";
            this.dotButton.UseVisualStyleBackColor = false;
            this.dotButton.Click += new System.EventHandler(this.dotButton_Click);
            // 
            // equalsButton
            // 
            this.equalsButton.BackColor = System.Drawing.Color.DarkGreen;
            this.equalsButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.equalsButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.equalsButton.Location = new System.Drawing.Point(378, 560);
            this.equalsButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.equalsButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.equalsButton.Name = "equalsButton";
            this.equalsButton.Size = new System.Drawing.Size(120, 60);
            this.equalsButton.TabIndex = 6;
            this.equalsButton.Text = "=";
            this.equalsButton.UseVisualStyleBackColor = false;
            this.equalsButton.Click += new System.EventHandler(this.equalsButton_Click);
            // 
            // twoButton
            // 
            this.twoButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.twoButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.twoButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.twoButton.Location = new System.Drawing.Point(126, 494);
            this.twoButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.twoButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.twoButton.Name = "twoButton";
            this.twoButton.Size = new System.Drawing.Size(120, 60);
            this.twoButton.TabIndex = 7;
            this.twoButton.Text = "2";
            this.twoButton.UseVisualStyleBackColor = false;
            this.twoButton.Click += new System.EventHandler(this.twoButton_Click);
            // 
            // threeButton
            // 
            this.threeButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.threeButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.threeButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.threeButton.Location = new System.Drawing.Point(252, 494);
            this.threeButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.threeButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.threeButton.Name = "threeButton";
            this.threeButton.Size = new System.Drawing.Size(120, 60);
            this.threeButton.TabIndex = 8;
            this.threeButton.Text = "3";
            this.threeButton.UseVisualStyleBackColor = false;
            this.threeButton.Click += new System.EventHandler(this.threeButton_Click);
            // 
            // plusButton
            // 
            this.plusButton.BackColor = System.Drawing.Color.DarkSlateBlue;
            this.plusButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.plusButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.plusButton.Location = new System.Drawing.Point(378, 494);
            this.plusButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.plusButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.plusButton.Name = "plusButton";
            this.plusButton.Size = new System.Drawing.Size(120, 60);
            this.plusButton.TabIndex = 9;
            this.plusButton.Text = "+";
            this.plusButton.UseVisualStyleBackColor = false;
            this.plusButton.Click += new System.EventHandler(this.plusButton_Click);
            // 
            // minusButton
            // 
            this.minusButton.BackColor = System.Drawing.Color.DarkSlateBlue;
            this.minusButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.minusButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.minusButton.Location = new System.Drawing.Point(378, 428);
            this.minusButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.minusButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.minusButton.Name = "minusButton";
            this.minusButton.Size = new System.Drawing.Size(120, 60);
            this.minusButton.TabIndex = 13;
            this.minusButton.Text = "-";
            this.minusButton.UseVisualStyleBackColor = false;
            this.minusButton.Click += new System.EventHandler(this.minusButton_Click);
            // 
            // sixButton
            // 
            this.sixButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.sixButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.sixButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.sixButton.Location = new System.Drawing.Point(252, 428);
            this.sixButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.sixButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.sixButton.Name = "sixButton";
            this.sixButton.Size = new System.Drawing.Size(120, 60);
            this.sixButton.TabIndex = 12;
            this.sixButton.Text = "6";
            this.sixButton.UseVisualStyleBackColor = false;
            this.sixButton.Click += new System.EventHandler(this.sixButton_Click);
            // 
            // fiveButton
            // 
            this.fiveButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.fiveButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.fiveButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.fiveButton.Location = new System.Drawing.Point(126, 428);
            this.fiveButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.fiveButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.fiveButton.Name = "fiveButton";
            this.fiveButton.Size = new System.Drawing.Size(120, 60);
            this.fiveButton.TabIndex = 11;
            this.fiveButton.Text = "5";
            this.fiveButton.UseVisualStyleBackColor = false;
            this.fiveButton.Click += new System.EventHandler(this.fiveButton_Click);
            // 
            // fourButton
            // 
            this.fourButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.fourButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.fourButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.fourButton.Location = new System.Drawing.Point(0, 428);
            this.fourButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.fourButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.fourButton.Name = "fourButton";
            this.fourButton.Size = new System.Drawing.Size(120, 60);
            this.fourButton.TabIndex = 10;
            this.fourButton.Text = "4";
            this.fourButton.UseVisualStyleBackColor = false;
            this.fourButton.Click += new System.EventHandler(this.fourButton_Click);
            // 
            // timesButton
            // 
            this.timesButton.BackColor = System.Drawing.Color.DarkSlateBlue;
            this.timesButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.timesButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.timesButton.Location = new System.Drawing.Point(378, 362);
            this.timesButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.timesButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.timesButton.Name = "timesButton";
            this.timesButton.Size = new System.Drawing.Size(120, 60);
            this.timesButton.TabIndex = 17;
            this.timesButton.Text = "x";
            this.timesButton.UseVisualStyleBackColor = false;
            this.timesButton.Click += new System.EventHandler(this.timesButton_Click);
            // 
            // nineButton
            // 
            this.nineButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.nineButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.nineButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.nineButton.Location = new System.Drawing.Point(252, 362);
            this.nineButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.nineButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.nineButton.Name = "nineButton";
            this.nineButton.Size = new System.Drawing.Size(120, 60);
            this.nineButton.TabIndex = 16;
            this.nineButton.Text = "9";
            this.nineButton.UseVisualStyleBackColor = false;
            this.nineButton.Click += new System.EventHandler(this.nineButton_Click);
            // 
            // eightButton
            // 
            this.eightButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.eightButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.eightButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.eightButton.Location = new System.Drawing.Point(126, 362);
            this.eightButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.eightButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.eightButton.Name = "eightButton";
            this.eightButton.Size = new System.Drawing.Size(120, 60);
            this.eightButton.TabIndex = 15;
            this.eightButton.Text = "8";
            this.eightButton.UseVisualStyleBackColor = false;
            this.eightButton.Click += new System.EventHandler(this.eightButton_Click);
            // 
            // sevenButton
            // 
            this.sevenButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.sevenButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.sevenButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.sevenButton.Location = new System.Drawing.Point(0, 362);
            this.sevenButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.sevenButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.sevenButton.Name = "sevenButton";
            this.sevenButton.Size = new System.Drawing.Size(120, 60);
            this.sevenButton.TabIndex = 14;
            this.sevenButton.Text = "7";
            this.sevenButton.UseVisualStyleBackColor = false;
            this.sevenButton.Click += new System.EventHandler(this.sevenButton_Click);
            // 
            // divisionButton
            // 
            this.divisionButton.BackColor = System.Drawing.Color.DarkSlateBlue;
            this.divisionButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.divisionButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.divisionButton.Location = new System.Drawing.Point(378, 296);
            this.divisionButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.divisionButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.divisionButton.Name = "divisionButton";
            this.divisionButton.Size = new System.Drawing.Size(120, 60);
            this.divisionButton.TabIndex = 21;
            this.divisionButton.Text = "÷";
            this.divisionButton.UseVisualStyleBackColor = false;
            this.divisionButton.Click += new System.EventHandler(this.divisionButton_Click);
            // 
            // squareRoot
            // 
            this.squareRoot.BackColor = System.Drawing.Color.DarkSlateBlue;
            this.squareRoot.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.squareRoot.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.squareRoot.Location = new System.Drawing.Point(252, 296);
            this.squareRoot.MaximumSize = new System.Drawing.Size(120, 60);
            this.squareRoot.MinimumSize = new System.Drawing.Size(120, 60);
            this.squareRoot.Name = "squareRoot";
            this.squareRoot.Size = new System.Drawing.Size(120, 60);
            this.squareRoot.TabIndex = 20;
            this.squareRoot.Text = "√";
            this.squareRoot.UseVisualStyleBackColor = false;
            this.squareRoot.Click += new System.EventHandler(this.squareRoot_Click);
            // 
            // squareButton
            // 
            this.squareButton.BackColor = System.Drawing.Color.DarkSlateBlue;
            this.squareButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.squareButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.squareButton.Location = new System.Drawing.Point(126, 296);
            this.squareButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.squareButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.squareButton.Name = "squareButton";
            this.squareButton.Size = new System.Drawing.Size(120, 60);
            this.squareButton.TabIndex = 19;
            this.squareButton.Text = "^";
            this.squareButton.UseVisualStyleBackColor = false;
            this.squareButton.Click += new System.EventHandler(this.squareButton_Click);
            // 
            // backupButton
            // 
            this.backupButton.BackColor = System.Drawing.Color.Maroon;
            this.backupButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.backupButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.backupButton.Location = new System.Drawing.Point(378, 230);
            this.backupButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.backupButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.backupButton.Name = "backupButton";
            this.backupButton.Size = new System.Drawing.Size(120, 60);
            this.backupButton.TabIndex = 25;
            this.backupButton.Text = "←";
            this.backupButton.UseVisualStyleBackColor = false;
            this.backupButton.Click += new System.EventHandler(this.backupButton_Click);
            // 
            // clearAllButton
            // 
            this.clearAllButton.BackColor = System.Drawing.Color.Maroon;
            this.clearAllButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.clearAllButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.clearAllButton.Location = new System.Drawing.Point(378, 164);
            this.clearAllButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.clearAllButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.clearAllButton.Name = "clearAllButton";
            this.clearAllButton.Size = new System.Drawing.Size(120, 60);
            this.clearAllButton.TabIndex = 24;
            this.clearAllButton.Text = "C";
            this.clearAllButton.UseVisualStyleBackColor = false;
            this.clearAllButton.Click += new System.EventHandler(this.clearAllButton_Click);
            // 
            // clearEntryButton
            // 
            this.clearEntryButton.BackColor = System.Drawing.Color.Maroon;
            this.clearEntryButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.clearEntryButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.clearEntryButton.Location = new System.Drawing.Point(252, 164);
            this.clearEntryButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.clearEntryButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.clearEntryButton.Name = "clearEntryButton";
            this.clearEntryButton.Size = new System.Drawing.Size(120, 60);
            this.clearEntryButton.TabIndex = 23;
            this.clearEntryButton.Text = "CE";
            this.clearEntryButton.UseVisualStyleBackColor = false;
            this.clearEntryButton.Click += new System.EventHandler(this.clearEntryButton_Click);
            // 
            // percentButton
            // 
            this.percentButton.BackColor = System.Drawing.Color.DarkSlateBlue;
            this.percentButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.percentButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.percentButton.Location = new System.Drawing.Point(0, 296);
            this.percentButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.percentButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.percentButton.Name = "percentButton";
            this.percentButton.Size = new System.Drawing.Size(120, 60);
            this.percentButton.TabIndex = 22;
            this.percentButton.Text = "%";
            this.percentButton.UseVisualStyleBackColor = false;
            this.percentButton.Click += new System.EventHandler(this.percentButton_Click);
            // 
            // rightParenButton
            // 
            this.rightParenButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(0)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.rightParenButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.rightParenButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.rightParenButton.Location = new System.Drawing.Point(126, 164);
            this.rightParenButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.rightParenButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.rightParenButton.Name = "rightParenButton";
            this.rightParenButton.Size = new System.Drawing.Size(120, 60);
            this.rightParenButton.TabIndex = 27;
            this.rightParenButton.Text = ")";
            this.rightParenButton.UseVisualStyleBackColor = false;
            this.rightParenButton.Click += new System.EventHandler(this.rightParenButton_Click);
            // 
            // leftParenButton
            // 
            this.leftParenButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(0)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.leftParenButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.leftParenButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.leftParenButton.Location = new System.Drawing.Point(0, 164);
            this.leftParenButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.leftParenButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.leftParenButton.Name = "leftParenButton";
            this.leftParenButton.Size = new System.Drawing.Size(120, 60);
            this.leftParenButton.TabIndex = 26;
            this.leftParenButton.Text = "(";
            this.leftParenButton.UseVisualStyleBackColor = false;
            this.leftParenButton.Click += new System.EventHandler(this.leftParenButton_Click);
            // 
            // piButton
            // 
            this.piButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.piButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.piButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.piButton.Location = new System.Drawing.Point(0, 230);
            this.piButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.piButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.piButton.Name = "piButton";
            this.piButton.Size = new System.Drawing.Size(120, 60);
            this.piButton.TabIndex = 28;
            this.piButton.Text = "π";
            this.piButton.UseVisualStyleBackColor = false;
            this.piButton.Click += new System.EventHandler(this.piButton_Click);
            // 
            // eButton
            // 
            this.eButton.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.eButton.FlatAppearance.BorderColor = System.Drawing.Color.Black;
            this.eButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 16.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.eButton.Location = new System.Drawing.Point(126, 230);
            this.eButton.MaximumSize = new System.Drawing.Size(120, 60);
            this.eButton.MinimumSize = new System.Drawing.Size(120, 60);
            this.eButton.Name = "eButton";
            this.eButton.Size = new System.Drawing.Size(120, 60);
            this.eButton.TabIndex = 29;
            this.eButton.Text = "e";
            this.eButton.UseVisualStyleBackColor = false;
            this.eButton.Click += new System.EventHandler(this.eButton_Click);
            // 
            // errorLabel
            // 
            this.errorLabel.AutoEllipsis = true;
            this.errorLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.errorLabel.ForeColor = System.Drawing.Color.Red;
            this.errorLabel.Location = new System.Drawing.Point(22, 121);
            this.errorLabel.MaximumSize = new System.Drawing.Size(440, 30);
            this.errorLabel.MinimumSize = new System.Drawing.Size(440, 30);
            this.errorLabel.Name = "errorLabel";
            this.errorLabel.Size = new System.Drawing.Size(550, 38);
            this.errorLabel.TabIndex = 30;
            this.errorLabel.TextAlign = System.Drawing.ContentAlignment.TopCenter;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.Desktop;
            this.ClientSize = new System.Drawing.Size(526, 620);
            this.Controls.Add(this.errorLabel);
            this.Controls.Add(this.eButton);
            this.Controls.Add(this.piButton);
            this.Controls.Add(this.rightParenButton);
            this.Controls.Add(this.leftParenButton);
            this.Controls.Add(this.backupButton);
            this.Controls.Add(this.clearAllButton);
            this.Controls.Add(this.clearEntryButton);
            this.Controls.Add(this.percentButton);
            this.Controls.Add(this.divisionButton);
            this.Controls.Add(this.squareRoot);
            this.Controls.Add(this.squareButton);
            this.Controls.Add(this.timesButton);
            this.Controls.Add(this.nineButton);
            this.Controls.Add(this.eightButton);
            this.Controls.Add(this.sevenButton);
            this.Controls.Add(this.minusButton);
            this.Controls.Add(this.sixButton);
            this.Controls.Add(this.fiveButton);
            this.Controls.Add(this.fourButton);
            this.Controls.Add(this.plusButton);
            this.Controls.Add(this.threeButton);
            this.Controls.Add(this.twoButton);
            this.Controls.Add(this.equalsButton);
            this.Controls.Add(this.dotButton);
            this.Controls.Add(this.oneButton);
            this.Controls.Add(this.zeroButton);
            this.Controls.Add(this.toggleSignButton);
            this.Controls.Add(this.resultLabel);
            this.Controls.Add(this.inputBox);
            this.ForeColor = System.Drawing.SystemColors.Window;
            this.Name = "Form1";
            this.Text = "Steve\'s Calculator";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox inputBox;
        private System.Windows.Forms.Label resultLabel;
        private System.Windows.Forms.Button toggleSignButton;
        private System.Windows.Forms.Button zeroButton;
        private System.Windows.Forms.Button oneButton;
        private System.Windows.Forms.Button dotButton;
        private System.Windows.Forms.Button equalsButton;
        private System.Windows.Forms.Button twoButton;
        private System.Windows.Forms.Button threeButton;
        private System.Windows.Forms.Button plusButton;
        private System.Windows.Forms.Button minusButton;
        private System.Windows.Forms.Button sixButton;
        private System.Windows.Forms.Button fiveButton;
        private System.Windows.Forms.Button fourButton;
        private System.Windows.Forms.Button timesButton;
        private System.Windows.Forms.Button nineButton;
        private System.Windows.Forms.Button eightButton;
        private System.Windows.Forms.Button sevenButton;
        private System.Windows.Forms.Button divisionButton;
        private System.Windows.Forms.Button squareRoot;
        private System.Windows.Forms.Button squareButton;
        private System.Windows.Forms.Button backupButton;
        private System.Windows.Forms.Button clearAllButton;
        private System.Windows.Forms.Button clearEntryButton;
        private System.Windows.Forms.Button percentButton;
        private System.Windows.Forms.Button rightParenButton;
        private System.Windows.Forms.Button leftParenButton;
        private System.Windows.Forms.Button piButton;
        private System.Windows.Forms.Button eButton;
        private System.Windows.Forms.Label errorLabel;
    }
}

