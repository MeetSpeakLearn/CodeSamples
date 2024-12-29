import { DataService } from '../data.service'; // Import the data service
import { Global } from "../common/global";
import { File } from "../common/file";
import { DirectoryType } from "../common/directorytype";

export class Folder {
    name: string = "";
    type: DirectoryType = DirectoryType.UNASSIGNED;
    files: Array<string> = [];

    public static parseDirectoryType(str: string) : DirectoryType {
        switch (str.toLowerCase()) {
            case 'unassigned': return DirectoryType.UNASSIGNED;
            case 'upload': return DirectoryType.UPLOAD;
            case 'copyfiles': return DirectoryType.COPY;
            case 'memberdir': return DirectoryType.MEMBER_DIR;
            case 'member': return DirectoryType.MEMBER;
            case 'uploadedfiles': return DirectoryType.UPLOADED_FILES;
            default: return DirectoryType.UNASSIGNED;
        }
    }

    constructor(obj: any = null, private dataService: DataService) {
        if (obj !== null) {
            this.name = obj.name;
            this.type = Folder.parseDirectoryType(obj.type);

            this.dataService.getdir(Global.username, Global.password, this.type, this.name).subscribe((data) => {
                this.files = data.sort((a: any, b: any) => Number(a) - Number(b));
              }, (error) => {
                console.error('There was an error retrieving data:', error);
              });
        }
    }

    addFile(fileName: string) {
        var index = this.files.indexOf(fileName);
        if (index == -1) {
            this.files.push(fileName);
        }
    }
}