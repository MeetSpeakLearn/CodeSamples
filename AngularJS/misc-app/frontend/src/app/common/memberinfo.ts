import { DataService } from '../data.service'; // Import the data service

export class MemberInfo {
    fname: string = '';
    lname: string = '';
    middle: string = '';

    constructor(username: string, password: string, private dataService: DataService) {
        console.log("new MemberInfo()");
        this.dataService.userInfo(username, password).subscribe((data) => {
            if (data.length > 0) {
                var rows: any[] = data[0];
                if (rows.length > 0) {
                    var row: any = rows[0];
                    this.fname = row['fname'];
                    this.lname = row['lname'];
                    this.middle = row['middle'];
                }
            }
            console.log("fname is " + this.fname);
      }, (error) => {
        console.error('There was an error retrieving data:', error);
      });
    }
}
