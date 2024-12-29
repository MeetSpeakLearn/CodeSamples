import { Component, Input, OnInit } from '@angular/core';
import { DataService } from '../data.service'; // Import the data service
import { CommonModule } from '@angular/common';
import { Global } from "../common/global";
import { Telephone } from "../common/telephone";

@Component({
    selector: 'app-institutioninfo',
    templateUrl: './institutioninfo.component.html',
    styleUrls: ['./institutioninfo.component.css'],
    standalone: true,
    imports: [ CommonModule ]
  })
  export class InstitutionInfoComponent implements OnInit {
    /*
        Member Number
        Organization Name
        Telephone
        FAX
        Email
        Address
        Address2
        City
        State
        Zip
    */

    member: Number = 0;
    organizationName: string = "";
    fname: string = "";
    lname: string = "";
    title: string = "";
    @Input() username: string = "";
    @Input() password: string = "";
    telephone: string = "";
    fax: string = "";
    email: string = "";
    address: string = "";
    address2: string = "";
    city: string = "";
    state: string = "";
    zip: string = "";
    @Input() callID: any = null;

    constructor(private dataService: DataService) {}

    ngOnInit(): void {
        if (this.callID !== null) {
            this.member = this.callID;
        } else {
            this.member = Global.memberNumber;
        }

        if ((this.username == null) || (this.username == "")) {
            this.username = Global.username;
        }

        if ((this.password == null) || (this.password == "")) {
            this.password = Global.password;
        }

        console.log("institutionInfo:");
        console.log("this.member=" + this.member);
        console.log("this.username=" + this.username);
        console.log("this.password=" + this.password);

        this.dataService.institutionInfo(this.member, this.username, this.password).subscribe((data) => {
            if (data.length > 0) {
                var rows: Array<any> = data[0];

                if (rows.length > 0) {
                    var row: any = rows[0];

                    this.organizationName = row['organization'] ?? '';
                    this.fname = row['fname'] ?? '';
                    this.lname = row['lname'] ?? '';
                    this.title = row['title'] ?? '';
                    this.username = row['username'] ?? '';
                    this.telephone = row['phone'] ?? '';
                    this.fax = row['fax'] ?? '';
                    this.email = row['email'] ?? '';
                    this.address = row['address'] ?? '';
                    this.address2 = row['address2'] ?? '';
                    this.city = row['city'] ?? '';
                    this.state = row['state'] ?? '';
                    this.zip = row['zip'] ?? '';
                    return;
                }
            }

            this.organizationName = '';
            this.fname = '';
            this.lname = '';
            this.title = '';
            this.username = '';
            this.telephone = '';
            this.fax = '';
            this.email = '';
            this.address = '';
            this.address2 = '';
            this.city = '';
            this.state = '';
            this.zip = '';
          }, (error) => {
            console.error('There was an error retrieving data:', error);
          });
    }

    formattedAddress(): string {
        if (this.address != '') {
            if (this.address2 != '') {
                return this.address + ', ' + this.address2;
            } else {
                return this.address;
            }
        }

        return '';
    }

    formattedTelephone(): string {
        return Telephone.formatNorthAmerican(this.telephone);
    }

    formattedFax(): string {
        return Telephone.formatNorthAmerican(this.fax);
    }
  }