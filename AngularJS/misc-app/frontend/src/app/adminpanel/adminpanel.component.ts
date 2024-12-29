import { Component, Input, OnInit } from '@angular/core';
import { FormsModule} from '@angular/forms';
import { DataService } from '../data.service'; // Import the data service
import { UploadService } from '../upload.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Global } from "../common/global";
import { MemberInfo } from "../common/memberinfo";
import { DirectoryType } from "../common/directorytype";
import { Folder } from "../common/folder";
import { CreateNewClientComponent } from "../createnewclient/createnewclient.component";

@Component({
    selector: 'app-adminpanel',
    templateUrl: './adminpanel.component.html',
    styleUrls: ['./adminpanel.component.css'],
    standalone: true,
    imports: [ FormsModule, CommonModule, CreateNewClientComponent ]
  })
  export class AdminPanelComponent implements OnInit {
    data: any[] = []; // Property to store the data
    id: any = null;
    username: any = '';
    password: any = '';
    fname: string | null | undefined = '';
    selectedFile: File | null = null;
    copyfiles: Folder | null = null;
    // uploadedfiles: Folder | null = null;
    memberdir: Folder | null = null;
    uploadsdir: Folder | null = null;

    selectedMember: string = '';
    selectedMemberFolder: Folder | null = null;

    selectedMemberUploads: string = '';
    selectedMemberUploadsFolder: Folder | null = null;

    activeMembers: Array<any> | null = null;

    newClient: CreateNewClientComponent | null = null;

    constructor(private dataService: DataService, private uploadService: UploadService, private activatedroute:ActivatedRoute) {
      // this.newClient = new CreateNewClientComponent(this.dataService, this.activatedroute);
      
      this.id = this.activatedroute.snapshot.paramMap.get("id");
      this.username = this.activatedroute.snapshot.paramMap.get("username");
      this.password = this.activatedroute.snapshot.paramMap.get("password");

      if (this.id == null) {
        this.id = Global.memberNumber;
      } else {
        Global.memberNumber = this.id;
      }

      if (this.username == null) {
        this.id = Global.username;
      } else {
        Global.username = this.username;
      }

      if (this.password == null) {
        this.password = Global.password;
      } else {
        Global.password = this.password;
      }
    }
    
    ngOnInit(): void {
      this.fname = Global.institution?.fname;
      
      var memberInfo: MemberInfo = new MemberInfo(this.username, this.password, this.dataService);
      this.fname = memberInfo.fname;
      console.log("fname is " + this.fname + "!");
      /*
      if ((this.fname == null) || (this.fname == undefined)|| (this.fname == '')) {
        this.fname = "Unknown";
        console.log("fname is unknown!");
      }
      else {
        var memberInfo: MemberInfo = new MemberInfo(this.username, this.password, this.dataService);
        this.fname = memberInfo.fname;
        console.log("fname is " + this.fname + "!");
      }
      */

      this.copyfiles = new Folder({name: "copyfiles", type: DirectoryType.COPY}, this.dataService);
      this.memberdir = new Folder({name: "memberdir", type: DirectoryType.MEMBER_DIR}, this.dataService);
      this.uploadsdir = new Folder({name: "uploads", type: DirectoryType.UPLOAD}, this.dataService);
      // this.uploadedfiles = new Folder({name: "uploadedfiles", type: DirectoryType.UPLOADED_FILES}, this.dataService);

      this.dataService.activeMembers(Global.username, Global.password).subscribe((data) => {
          this.activeMembers = data;
        }, (error) => {
          console.error('There was an error retrieving data:', error);
        });
    }

    getCopyFiles(): Array<string> {
      if (this.copyfiles !== null) {
        return this.copyfiles.files;
      } else {
        return [];
      }
    }

    getActiveMembers(): Array<string> {
      if (this.activeMembers !== null) {
        return this.activeMembers;
      } else
        return [];
    }

    getMemberDir(): Array<string> {
      if (this.memberdir !== null) {
        return this.memberdir.files;
      } else {
        return [];
      }
    }

    getUploadsDir(): Array<string> {
      if (this.uploadsdir !== null) {
        return this.uploadsdir.files;
      } else {
        return [];
      }
    }

    getSelectedMember(): string {
      if (this.selectedMember == "")
        return "Unselected";
      else
        return this.selectedMember;
    }

    setSelectedMember(e:any): void {
        if (e.target.value !== null) {
            if (typeof e.target.value == "string") {
                var asString: string = String(e.target.value);
                var index: number = asString.indexOf(': ');
                var memberPortionAsText: string = asString.substring(index + 2);
                console.log("memberPortionAsText is: " + memberPortionAsText);
                if (memberPortionAsText == "Unselected") {
                    this.selectedMember = '';
                    this.selectedMemberFolder = null;
                } else {
                  this.selectedMember = memberPortionAsText;
                  console.log("The selected member is: " + this.selectedMember);
                  this.selectedMemberFolder = new Folder({name: this.selectedMember, type: DirectoryType.MEMBER}, this.dataService);
                }
            }
        }
    }

    getSelectedMemberUploads(): string {
      if (this.selectedMemberUploads == "")
        return "Unselected";
      else
        return this.selectedMemberUploads;
    }

    setSelectedMemberUploads(e:any): void {
        if (e.target.value !== null) {
            if (typeof e.target.value == "string") {
                var asString: string = String(e.target.value);
                var index: number = asString.indexOf(': ');
                var memberPortionAsText: string = asString.substring(index + 2);
                console.log("memberPortionAsText is: " + memberPortionAsText);
                if (memberPortionAsText == "Unselected") {
                    this.selectedMemberUploads = '';
                    this.selectedMemberUploadsFolder = null;
                } else {
                  this.selectedMemberUploads = memberPortionAsText;
                  console.log("The selected member is: " + this.selectedMemberUploads);
                  this.selectedMemberUploadsFolder = new Folder({name: this.selectedMemberUploads, type: DirectoryType.UPLOADED_FILES}, this.dataService);
                }
            }
        }
    }

    hasUploadsDirectory(str:string): boolean {
      var index = this.getUploadsDir().indexOf(str);
      console.log("hasUploadsDirectory('" + str + "'), index=" + index);
      return index > -1;
    }

    getSelectedMemberDir(): Array<string> {
      if (this.selectedMember == "")
        return [];
      else if (this.selectedMemberFolder !== null)
        return this.selectedMemberFolder.files;
      else
        return [];
    }

    getSelectedMemberUploadsDir(): Array<string> {
      if (this.selectedMemberUploads == "")
        return [];
      else if (this.selectedMemberUploadsFolder !== null)
        return this.selectedMemberUploadsFolder.files;
      else
        return [];
    }
    
    selectTab(e:any, tabName: string): void {      
      var i: number = 0;
      var tabcontent: HTMLCollectionOf<Element> = document.getElementsByClassName("tabcontent");
      var tablinks: HTMLCollectionOf<Element> = document.getElementsByClassName("tablinks");

      for (i = 0; i < tabcontent.length; i++) {
        var elem: any = tabcontent[i];
        elem.style.display = "none";
      }

      for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
      }

      var tabElement :HTMLElement | null = document.getElementById(tabName);

      if (tabElement !== null) {
        tabElement.style.display = "block";
      }
    }

    createUploadDirectory(e:any): void {
      var name: string = this.getSelectedMemberUploads();
      this.dataService.makedir(Global.username, Global.password, DirectoryType.UPLOADED_FILES, name).subscribe((data) => {
        if (this.uploadsdir !== null) {
          this.uploadsdir.addFile(name);
          console.log("Added " + name + " to uploads directories");
        }
      }, (error) => {
        console.error('There was an error retrieving data:', error);
        window.confirm("Failed to create directory for " + name + " uploads.");
      });
    }

    onFileSelected(e:any) {
      if(e.target.files.length > 0) 
       {
        console.log("onFileSelected(): target=" + JSON.stringify(e.target));
        console.log("onFileSelected(): target.files=" + JSON.stringify(e.target.files));
        console.log("onFileSelected(): target.files[0]=" + JSON.stringify(e.target.files[0]));
         console.log("onFileSelected(): " + e.target.files[0].name);
         this.selectedFile = e.target.files[0];
         for (const key in this.selectedFile) {
          console.log("key=" + key);
         }
         console.log("type=" + this.selectedFile?.type);
         console.log("selectedFile=" + JSON.stringify(this.selectedFile));
         this.fname = this.selectedFile?.name;
       }
     }

    uploadSingleFile(e:any): void {
      var file:File | null = this.selectedFile;

      var fileName: string = '';
      var name: string = this.getSelectedMemberUploads();

      if (file) {
        fileName = file.name;

        var formData = new FormData();

        formData.append('Content-Type', 'multipart/form-data');
        formData.append('memberId', this.getSelectedMemberUploads());
        formData.append('file', file);

        console.log("uploadSingleFile(): formData=" + JSON.stringify(formData));

        console.log('file=' + file + ', fileName=' + fileName);

        this.uploadService.uploadFile(Global.username, Global.password, fileName, DirectoryType.UPLOADED_FILES, name, formData)
          .subscribe(
            (response) => {
              console.log('File uploaded successfully:', response);
            },
            (error) => {
              console.error('Error uploading file:', error);
            }
          );
      }
    }

    distribute(e:any): void {
      
    }

    addNew(e:any): void {
      
    }
  }

