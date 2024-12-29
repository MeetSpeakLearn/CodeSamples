// Added via: ng generate service data

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Global } from "./common/global"
import { DirectoryType } from "./common/directorytype"
// import { FileSaverModule, FileSaverService } from 'ngx-filesaver';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private dataUrl = 'http://localhost:3000/upload';  // The URL to the backend endpoint

  constructor(private http: HttpClient) { }

  uploadFile(userId : string, password : string, sourcepath : string, type: DirectoryType, name: string = '', formData: FormData): Observable<any> {
    // formData.append('userId', userId);
    // formData.append('password', password);
    // formData.append('sourcepath', sourcepath);
    // formData.append('type', type.toString());
    // formData.append('name', name);
    console.log("formData=" + JSON.stringify(formData));
    return this.http.post<any>(this.dataUrl, formData);
  }
}