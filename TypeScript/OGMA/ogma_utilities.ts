
function ogmaIndexOf(arr: Array<any>, v: any):number {
    let len: number = arr.length;
    let i:number;

    if (typeof v == "string") {
        for (i = 0; i < len; i++)
            if (typeof arr[i] == "string")
                if ((v as string).localeCompare(arr[i] as string) == 0)
                    return i;
        return -1;
    } else {
        for (i = 0; i < len; i++)
            if (arr[i] == v) return i;

        return -1;
    }
}

function ogmaArrayDifference(arr1: Array<any>, arr2: Array<any>): Array<any> {
    let result: Array<any> = new Array<any>();
    let count: number = arr1.length;

    for (let i:number = 0; i < count; i++) {
        if (ogmaIndexOf(arr2, arr1[i]) == -1) {
            result.push(arr1[i]);
        }
    }

    return result;
}

function enumValueToInt(enumeratedType: any, enumeratedTypeValue: any): any {
    let valueAsString: string = enumeratedTypeValue.toString();

    for (const key in enumeratedType) {
        if (key.localeCompare(valueAsString) == 0) {
            return enumeratedType[key];
        }
    }

    return null;
}

function beginsWithOneOf(text: string, chars: string) {
    return (chars.toLowerCase().search(text.toLowerCase().charAt(0)) != -1);
}
