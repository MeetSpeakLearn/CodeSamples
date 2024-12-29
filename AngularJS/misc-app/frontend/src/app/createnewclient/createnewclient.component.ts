import { Injectable, OnChanges, SimpleChanges } from '@angular/core';
import { Component, Input, Output, OnInit, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { DataService } from '../data.service';
import { Field } from "../common/field"
import { FormsModule } from '@angular/forms';

@Injectable({
    providedIn: 'root'
  })
  
@Component({
    selector: 'app-createnewclient',
    templateUrl: './createnewclient.component.html',
    styleUrls: ['./createnewclient.component.css'],
    standalone: true,
    imports: [CommonModule, FormsModule]
  })

  export class CreateNewClientComponent implements OnInit, OnChanges {
    fields: Array<Field> = [];
    
    constructor(private dataService: DataService,
        private activatedroute:ActivatedRoute) {
            console.log("new CreateNewClientComponent()");
    }

    ngOnInit(): void {        
        this.fields.push(new Field('organization', 'Organization', 'text', '', false));
        this.fields.push(new Field('call_num', 'Call Number', 'number', 999999, false));
        this.fields.push(new Field('parent_call_num', 'Parent Call Number', 'number', 0, false));
        this.fields.push(new Field('chart_type', 'Charter Type', 'number', 0, false));
        console.log("Field count is: " + this.fields.length);
    }

    ngOnChanges(changes: SimpleChanges) {}
  }