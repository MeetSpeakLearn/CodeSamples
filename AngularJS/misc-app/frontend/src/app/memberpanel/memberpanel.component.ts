import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { InstitutionInfoComponent } from '../institutioninfo/institutioninfo.component';
import { DirectoryComponent } from '../directory/directory.component';
import { Global } from "../common/global";

@Component({
    selector: 'app-memberpanel',
    templateUrl: './memberpanel.component.html',
    styleUrls: ['./memberpanel.component.css'],
    standalone: true,
    imports: [ CommonModule, InstitutionInfoComponent, DirectoryComponent ]
  })
export class MemberPanelComponent {
    id: any = null;
    username: any = null;
    password: any = null;

    constructor(private activatedroute:ActivatedRoute) {
        this.id = this.activatedroute.snapshot.paramMap.get("id");
        this.username = this.activatedroute.snapshot.paramMap.get("username");
        this.password = this.activatedroute.snapshot.paramMap.get("password");

        if (this.username == null) {
            this.username = Global.username;
        } else {
            Global.username = this.username;
        }

        if (this.password == null) {
            this.password = Global.password;
        } else {
            Global.password = this.password;
        }

        console.log("this.id=" + this.id);
        console.log("this.username=" + this.username);
        console.log("this.password=" + this.password);
    }
}
