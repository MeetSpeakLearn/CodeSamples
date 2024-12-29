import { Injectable, OnChanges, SimpleChanges } from '@angular/core';
import { Component, Input, Output, OnInit, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataService } from '../data.service'; // Import the data service
import { Global } from "../common/global";
import { CommonModule } from '@angular/common';
import { File } from "../common/file"
import { FormsModule } from '@angular/forms';
import { MemberPanelComponent } from '../memberpanel/memberpanel.component';

@Injectable({
  providedIn: 'root'
})

@Component({
    selector: 'app-directory',
    templateUrl: './directory.component.html',
    styleUrls: ['./directory.component.css'],
    standalone: true,
    imports: [CommonModule, FormsModule]
  })
  export class DirectoryComponent implements OnInit, OnChanges {
    data: Array<File> = []; // Property to store the data
    @Input() filteredData: Array<File> = [];
    years: Array<number> = [];
    extensions: Array<string> = [];
    title = 'directory';
    @Input() isByYear: boolean = false;
    @Input() isByExtension: boolean = false;
    @Input() isByDirector: boolean = false;
    @Input() filterYear: number = 0;
    @Input() filterExtension: string | null = null;
    @Input() callID: any = null;
    @Input() username: string = "";
    @Input() password: string = "";
  
    constructor(private dataService: DataService,
      private activatedroute:ActivatedRoute) {
      }
    
    ngOnInit(): void {
      this.callID = this.activatedroute.snapshot.paramMap.get("id");
      this.dataService.listClientDirectory(((this.callID !== null) && (this.callID !== undefined)) ? this.callID.toString() : Global.username, Global.password).subscribe((data) => {
        this.data = File.toArrayOfFile(data, this.dataService); // Assign the received data to the property
        this.filteredData = this.data;
        this.years = File.listOfYears(this.data);
        this.extensions = File.listOfExtensions(this.data);
      }, (error) => {
        console.error('There was an error retrieving data:', error);
      });
    }

    ngOnChanges(changes: SimpleChanges) {
      this.getFiles();
    }

    yearSelectText(): string {
      if (this.filterYear == 0)
        return "Any Year";
      else
        return "" + this.filterYear ;
    }

    extensionSelectText(): string {
      if (this.filterExtension === null)
        return "Any Extension";
      else if (this.filterExtension == "")
        return "Any Extension"
      else
        return "" + this.filterExtension ;
    }

    setByYearFilter(e:any): void {
        if (e.target.value !== null) {
            if (typeof e.target.value == "string") {
                var asString: string = String(e.target.value);
                var index: number = asString.indexOf(': ');
                var yearPortionAsText: string = asString.substring(index + 2);
                console.log("yearPortionAsText is: " + this.filterYear);
                if (yearPortionAsText == "Any Year") {
                    this.filterYear = 0;
                } else {
                  this.filterYear = parseInt(yearPortionAsText);
                  console.log("The year is: " + this.filterYear);
                }
            }
        }
        this.getFiles();
    }

    setByExtensionFilter(e:any): void {
        if (e.target.value !== null) {
            if (typeof e.target.value == "string") {
                var asString: string = String(e.target.value);
                var index: number = asString.indexOf(': ');
                var rawFilterExtension = asString.substring(index + 2);
                if (rawFilterExtension == "Any Extension") {
                  this.filterExtension = null;
                } else {
                  this.filterExtension = rawFilterExtension;
                }
                console.log("The extension is: " + this.filterExtension);
            }
        }
        this.getFiles();
    }

    getFiles(): Array<File> {
      var result: Array<File> = this.data

      if (this.isByYear && (this.filterYear != 0)) {
        result = File.filterByYear(this.filterYear, result);
      }

      if (this.isByExtension && (this.filterExtension !== null)) {
        result = File.filterByExtension(this.filterExtension, result);
      }

      if (this.isByDirector) {
        result = File.filterByDirector(this.isByDirector, result);
      }

      result = result.sort((a: File, b: File) => a.fullName.toLowerCase().localeCompare(b.fullName.toLowerCase()));

      this.filteredData = result;

      return result;
    }
  }
  