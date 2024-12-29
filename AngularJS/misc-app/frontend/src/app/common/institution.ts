export class Institution {
    id: string = '';
    memberNumber: number = 0;
    organization: string = '';
    fname: string = '';
    lname: string = '';
    username: string = '';
    testingpassword: string = '';

    constructor(obj: any = null) {
        if (obj instanceof Object) {
            this.id = obj.id ?? null;
            this.memberNumber = obj.call_num ?? 0;
            this.organization = obj.organization ?? null;
            this.fname = obj.fname ?? null;
            this.lname = obj.lname ?? null;
            this.username = obj.username ?? null;
        }
    }
}
