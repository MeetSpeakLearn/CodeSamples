export class Field {
    dbFieldName: string = '';
    displayFieldName: string = '';
    fieldType: string = 'any';
    optional: boolean = true;
    defaultValue: any = null;

    constructor(dbFName: string, dbDisplyFName: string = '', ft: string = 'any', dValue: any = null, optional: boolean = false) {
        this.dbFieldName = dbFName;

        if (dbDisplyFName == '') {
            this.displayFieldName = dbFName;
        } else {
            this.displayFieldName  = dbDisplyFName;
        }

        this.fieldType = ft;
        this.defaultValue = dValue;
        this.optional = optional;
    }
}