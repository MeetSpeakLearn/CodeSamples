import { Component, Input, OnInit } from '@angular/core';
import { FormsModule} from '@angular/forms';
import { DataService } from '../data.service'; // Import the data service
import { Institution } from "../common/institution";
import { Global } from "../common/global";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [ FormsModule ]
})
export class LoginComponent implements OnInit {
  data: any[] = []; // Property to store the data
  @Input() username: string = '';
  @Input() password: string = '';
  @Input() fname: string = '';
  @Input() loaded: boolean = false;
  @Input() accessDenied: boolean = false;
  @Input() error: boolean = false;
  @Input() applyForAnAccount: boolean = false;
  @Input() loginAction: string='NOTHING';

  constructor(private dataService: DataService) {} // Inject the data service
  
  ngOnInit(): void {
  }

  onSubmit() {
    // Implement your login logic here
    console.log('Username:', this.username);
    console.log('Password:', this.password);
    // Add authentication logic and navigate to the next page upon successful login

    this.dataService.login(this.username, this.password).subscribe((data) => {
      this.data = data; // Assign the received data to the property
      this.loaded = false;
      this.accessDenied = false;
      this.error = false;

      if (data.length > 0)  {
        Global.institution = new Institution(data[0]);
        if (!((this.username == null) || (this.username == ""))) {
          Global.username = this.username;
          Global.memberNumber = Global.institution.memberNumber;
          Global.password = this.password;
        }
        this.fname = Global.institution.fname;
        this.loaded = true;
        this.accessDenied = false;
        this.error = false;
      } else {
        this.loaded = false;
        this.accessDenied = true;
      }
    }, (error) => {
      this.error = true;
      console.error('There was an error retrieving data:', error);
    });
  }

  onLogout(e: any) {
    window.location.reload();
  }

  onChangeAction(e: any) {
    this.loginAction = e.target.value;

    if (this.loginAction === "TryAgain") {
      window.location.reload();
    }
  }
}
