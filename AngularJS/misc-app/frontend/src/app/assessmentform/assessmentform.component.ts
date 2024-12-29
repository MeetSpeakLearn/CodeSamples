import { Injectable, OnChanges, SimpleChanges } from '@angular/core';
import { Component, Input, Output, OnInit } from '@angular/core';
import { DataService } from '../data.service'; // Import the data service
import { Global } from "../common/global";
import { CommonModule } from '@angular/common';
import { File } from "../common/file"
import { FormsModule } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})

@Component({
    selector: 'app-assessmentform',
    templateUrl: './assessmentform.component.html',
    styleUrls: ['./assessmentform.component.css'],
    standalone: true,
    imports: [CommonModule, FormsModule]
  })
  export class AssessmentFormComponent implements OnInit {
    data: any[] = []; // Property to store the data
    loaded: boolean = false;
    error: boolean = false;
    @Input() creditUnionName: string = "";
    @Input() address: string = "";
    @Input() address2: string = "";
    @Input() ncuaCharterNumber: string = "";
    @Input() date: string = "";
    @Input() totalSharesAndDesposits: number = 0.0;
    @Input() previousTotalSharesAndDesposits: number = 0.0;
    @Input() excessSharesAndDesposits: number = 0.0;
    @Input() previousExcessHigh: number = 0.0;
    @Input() increaseInSharesAndDesposits: number = 0.0;
    @Input() ncua5300Agress: string = "";
    @Input() ncua5300Memo: string = "";
    @Input() riskStandardCompliance: string = "";
    @Input() riskStandardMemo: string = "";
    @Input() certify: string = "";
    @Input() submitter: string = "";
    @Input() titlePosition: string = "";
    @Input() emailAddress: string = "";
    @Input() dateSubmitted: string = "";
  
    constructor(private dataService: DataService) {} // Inject the data service
    
    ngOnInit(): void {
        var username: string = Global.username;
        var password: string = Global.password;

        if ((username == null) || (username == "")) {
            console.log("AssessmentFormComponent: No username.");
            return;
        }

        this.dataService.assessmentFormData(username, password).subscribe((data) => {
            var temp: number;
            this.data = data; // Assign the received data to the property
            this.loaded = false;
            this.error = false;
      
            if (data.length > 0)  {
                var firstReturnValue: any = data[0];

                if (firstReturnValue !== null) {
                    if (Array.isArray(firstReturnValue)) {
                        var arrayOfObject: Array<any> = firstReturnValue as Array<any>;
                        if (arrayOfObject.length > 0) {
                            console.log(JSON.stringify(arrayOfObject));
                            var record: any = arrayOfObject[0];
                            this.creditUnionName = record.credit_union_name;
                            this.address = record.address;
                            this.address2 = record.address2;
                            this.ncuaCharterNumber = record.ncua_charter_number;

                            if (record.total_shares_and_desposits == null) {
                                this.previousTotalSharesAndDesposits = 0;
                            } else if (typeof record.total_shares_and_desposits == 'string') {
                                temp = parseFloat(record.total_shares_and_desposits);
                                if (Number.isNaN(temp)) {
                                    temp = 0;
                                }

                                this.previousTotalSharesAndDesposits = temp;
                            } else {
                                this.previousTotalSharesAndDesposits = record.total_shares_and_desposits;
                            }

                            if (record.previousExcessHigh == null) {
                                this.previousExcessHigh = 0;
                            } else if (typeof record.previousExcessHigh == 'string') {
                                temp = parseFloat(record.previousExcessHigh);
                                if (Number.isNaN(temp)) {
                                    temp = 0;
                                }

                                this.previousExcessHigh = temp;
                            } else {
                                this.previousExcessHigh = record.previousExcessHigh;
                            }
                            
                            this.emailAddress = record.email_address;
                            this.loaded = true;
                        } else {
                            console.log("ngOnInit:: No data returned.");
                        }
                    } else {
                        console.log("ngOnInit:: Malformed data returned.");
                    }
                } else {
                    console.log("ngOnInit:: Malformed data returned."); 
                }
            } else {
              this.loaded = false;
            }
          }, (error) => {
            this.error = true;
            console.error('There was an error retrieving data:', error);
          });
    }

    public computedExcessSharesAndDesposits(): number {
        return Math.max(this.totalSharesAndDesposits - this.previousTotalSharesAndDesposits, 0);
    }

    public getDateAsString(): string {
        const date = new Date();
        var year = date.toLocaleString("default", { year: "numeric" }).trim();
        var month = date.toLocaleString("default", { month: "2-digit" }).trim();
        var day = date.toLocaleString("default", { day: "2-digit" }).trim();
        var formattedDate = year + "-" + month + "-" + day;
        console.log("formattedDate=" + formattedDate);
        return formattedDate.trim();
    }

    public getDate(): Date {
        return new Date();
    }
  }
  