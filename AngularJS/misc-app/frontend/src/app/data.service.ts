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
export class DataService {
  private dataUrl = 'http://localhost:3000/data';  // The URL to the backend endpoint
  constructor(private http: HttpClient) { }
  // constructor(private http: HttpClient, private fileSaverService: FileSaverService) { }
  getData(): Observable<any[]> {
    return this.http.get<any[]>(this.dataUrl);    // Fetch data from the backend
  }
  login(username : string, password : string): Observable<any[]> {
    const params = new HttpParams()
      .set('query', 'login')
      .set('username', username)
      .set('password', password);
    return this.http.get<any[]>(this.dataUrl, {params});    // Fetch data from the backend
  }
  activeMembers(username : string, password : string): Observable<any[]> {
    const params = new HttpParams()
      .set('query', 'active_members')
      .set('username', username)
      .set('password', password);
    return this.http.get<any[]>(this.dataUrl, {params});    // Fetch data from the backend
  }
  listClientDirectory(username : string, password : string): Observable<any[]> {
    const params = new HttpParams()
      .set('query', 'clientDirectory')
      .set('username', username)
      .set('password', password);
    return this.http.get<any[]>(this.dataUrl, {params});    // Fetch data from the backend
  }
  getdir(username : string, password : string, type : DirectoryType, name: string = ''): Observable<any[]> {
    const params = new HttpParams()
      .set('query', 'getdir')
      .set('username', username)
      .set('password', password)
      .set('type', type.toString())
      .set('name', name);
    return this.http.get<any[]>(this.dataUrl, {params});    // Fetch data from the backend
  }
  makedir(username : string, password : string, type : DirectoryType, name: string = ''): Observable<any> {
    const params = new HttpParams()
      .set('query', 'makedir')
      .set('username', username)
      .set('password', password)
      .set('type', type.toString())
      .set('name', name);
      return this.http.get<any[]>(this.dataUrl, {params});
  }
  uploadFile(username : string, password : string, sourcepath : string, type: DirectoryType, name: string = '', formData: FormData): Observable<any> {
    formData.append('query', 'upload');
    formData.append('username', username);
    formData.append('password', password);
    formData.append('sourcepath', sourcepath);
    formData.append('type', type.toString());
    formData.append('name', name);
    console.log("formData=" + JSON.stringify(formData));
    return this.http.post<any>(this.dataUrl, formData);
  }
  downloadFile(username : string, password : string, fileName : string, mimeType: string): void {
    const params = new HttpParams()
      .set('query', 'download')
      .set('username', username)
      .set('password', password)
      .set('filename', fileName);
    let headers = new HttpHeaders();
    headers = headers.set('Accept', mimeType);
    this.http.get<any[]>(this.dataUrl, {params, headers: headers, responseType: 'blob' as 'json'}).subscribe(
      (response: any) =>{
          let dataType = response.type;
          // var blob = new Blob([response.data], { type: mimeType });
          let binaryData = [];
          binaryData.push(response);
          let downloadLink = document.createElement('a');
          downloadLink.href = window.URL.createObjectURL(new Blob(binaryData, {type: mimeType}));
          if (fileName)
              downloadLink.setAttribute('download', fileName);
          document.body.appendChild(downloadLink);
          downloadLink.click();
      });
  }
  assessmentFormData(username : string, password : string): Observable<any[]> {
    const params = new HttpParams()
      .set('query', 'assessmentFormDefaults')
      .set('username', username)
      .set('password', password);
    return this.http.get<any[]>(this.dataUrl, {params});    // Fetch data from the backend
  }
  institutionInfo(memberNumber : Number, username: string | null, password: string | null): Observable<any[]> {
    const params = new HttpParams()
      .set('query', 'institutionInfo')
      .set('memberNumber', memberNumber.toString())
      .set('username', (username == null) ? '' : username)
      .set('password', (password == null) ? '' : password);
    return this.http.get<any[]>(this.dataUrl, {params});    // Fetch data from the backend
  }
  userInfo(username: string, password: string):Observable<any[]> {
    const params = new HttpParams()
      .set('query', 'userinfo')
      .set('username', username)
      .set('password', password);
    return this.http.get<any[]>(this.dataUrl, {params});    // Fetch data from the backend
  }
}