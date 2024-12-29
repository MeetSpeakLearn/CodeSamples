
import { DataService } from '../data.service'; // Import the data service
import { Global } from "../common/global";

export class File {
    fullName: string = "";
    name: string = "";
    extension: string | null = null;
    directory: boolean = false;
    year: number = 0;
    month: number = 0;
    day: number = 0;

    public static ASSETS_PATH: string = "assets/";

    public static toArrayOfFile(arrayOfObjects: Array<any>, ds: DataService): Array<File> {
        var result: Array<File> = [];
        var obj: any;

        for (obj of arrayOfObjects) {
            if (obj !== null) {
                result.push(new File(obj, ds));
            }
        }

        return result;
    }

    public static filterByYear(year: number, files: Array<File>): Array<File> {
        var result: Array<File> = [];
        var file: File;

        for (file of files) {
            if (file.year == year) result.push(file);
        }

        return result;
    }

    public static filterByExtension(extension: string, files: Array<File>): Array<File> {
        var result: Array<File> = [];
        var file: File;

        for (file of files) {
            if (file.extension == extension) result.push(file);
        }

        return result;
    }

    public static filterByDirector(isDirector: boolean, files: Array<File>): Array<File> {
        var result: Array<File> = [];
        var file: File;

        for (file of files) {
            if (file.directory == isDirector) result.push(file);
        }

        return result;
    }

    public static listOfYears(files: Array<File>): Array<number> {
        var uniqueYears: Array<number> = [];
        var file: File;
        var year: number;
        var found = false;
        for (file of files) {
            found = false;
            for (year of uniqueYears) {
                if (file.year == year) {
                    found = true;
                    break;
                }
            }
            if (! found) {
                if (!Number.isNaN(file.year))
                    uniqueYears.push(file.year);
            }
        }

        return uniqueYears.sort((n1,n2) => n1 - n2);
    }

    public static listOfExtensions(files: Array<File>): Array<string> {
        var uniqueExtensions: Array<string> = [];
        var file: File;
        var extension: string;
        var found = false;
        for (file of files) {
            found = false;
            for (extension of uniqueExtensions) {
                if (file.extension == extension) {
                    found = true;
                    break;
                }
            }
            if (! found) {
                if (file.extension !== null)
                    uniqueExtensions.push(file.extension);
            }
        }

        return uniqueExtensions.sort();
    }

    constructor(obj: any = null, private dataService: DataService) {
        if (obj !== null) {
            this.fullName = obj.fullName;
            this.name = obj.name;
            this.extension = obj.extension;

            if ((obj.yearMonthDay !== null) && (obj.yearMonthDay !== undefined)) {
                var yearMonthDay: any = obj.yearMonthDay;
                var yearAsString: string | null = yearMonthDay[0];
                var monthAsString: string | null = yearMonthDay[1];
                var dayAsString: string | null = yearMonthDay[2];

                this.year = parseInt(String(yearAsString));
                this.month = parseInt(String(monthAsString));
                this.day = parseInt(String(dayAsString));

                this.directory = (yearMonthDay.length == 4) ? (yearMonthDay[3] == "directory") : false;
            }
        }
    }

    public getExtensionIconPath(): string {
        var extension: string = (this.extension !== null) ? this.extension.toLowerCase() : "";
        switch (extension) {
            case "pdf":
                return File.ASSETS_PATH + "images/pdf.jpg";
            case "pps":
                return File.ASSETS_PATH + "images/pps.jpg";
            case "zip":
                return File.ASSETS_PATH + "images/zip.jpg";
            case "txt":
                return File.ASSETS_PATH + "images/txt.jpg";
            case "":
                return File.ASSETS_PATH + "mages/none.jpg";
            default:
                return File.ASSETS_PATH + "images/other.jpg";
        }
    }

    public mimeType(): string | null {
        if (this.extension == null) return null;
        var extensionNormalized = this.extension.toLowerCase();
        switch (extensionNormalized) {
          case "pdf":
              return "application/pdf";
          case "pps":
          case "pot":
          case "ppt":
          case "ppz":
              return "application/mspowerpoint";
          case "doc":
          case "dot":
              return "application/msword";
          case "docx":
              return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
          case "zip":
              return "application/zip";
          case "7z":
              return "application/x-7z-compressed";
          case "gz":
              return "application/x-gzip";
          case "txt":
              return "text/plain";
          case "xls":
              return "application/vnd.ms-excel";
          case "xlsx":
              return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
          case "htm":
          case "html":
              return "text/html";
          case "csv":
              return "text/csv";
          default:
              return "text/plain";
        }
      }

    public download(): void {
        var fileName: string = (this.extension !== null) ? this.name + '.' + this.extension : this.name;
        var mimeType: string | null = this.mimeType();
        if (mimeType === null) mimeType = "text/plain";
        this.dataService.downloadFile(Global.username, Global.password, fileName, mimeType);
    }
}