
// applyMixins is from https://www.typescriptlang.org/docs/handbook/mixins.html
function applyMixins(derivedCtor: any, constructors: any[]) {
    constructors.forEach((baseCtor) => {
        Object.getOwnPropertyNames(baseCtor.prototype).forEach((name) => {
            Object.defineProperty(
                derivedCtor.prototype,
                name,
                Object.getOwnPropertyDescriptor(baseCtor.prototype, name)
            );
        });
    });
}

interface Allocator<T> {
    alloc(): T;
    dealloc(something: T);
}

interface Comparator<T> {
    compareTo(o1: T, o2: T): number;
}

class OgmaObject {
    static instanceCounter: number = 0;
    static instances: Array<OgmaObject> = new Array<OgmaObject>();
    static MIME_TYPE: string = "text/ogma";
    static MIME_TYPE_SOURCE: string = "text/ogma.source";
    static MIME_TYPE_DESTINATION: string = "text/ogma.destination";

    static isOgmaMimeType(something: string): boolean {
        let parsed: Array<string> = something.split(".");

        return (parsed[0].localeCompare(OgmaObject.MIME_TYPE) == 0);
    }

    static isOgmaSource(something: string): boolean {
        let parsed: Array<string> = something.split(".");

        if (parsed[0].localeCompare(OgmaObject.MIME_TYPE) == 0) {
            if (parsed.length < 2) return false;
           return (parsed[1].localeCompare("source") == 0);
        }

        return false;
    }

    static isOgmaDestination(something: string): boolean {
        let parsed: Array<string> = something.split(".");

        if (parsed[0].localeCompare(OgmaObject.MIME_TYPE) == 0) {
            if (parsed.length < 2) return false;
            return (parsed[1].localeCompare("destination") == 0);
        }

        return false;
    }

    protected readonly hashString: string;

    static getInstance(hashString: string): OgmaObject {
        let index: number = parseInt(hashString, 16);
        return OgmaObject.instances[index];
    }

    static get(hash:string): OgmaObject {
        if (hash.charCodeAt(0) != "ω".charCodeAt(0)) return null;
        return OgmaObject.instances[parseInt(hash.substr(1), 16)];
    }

    constructor() {
        this.hashString = "ω" + OgmaObject.instanceCounter.toString(16);
        OgmaObject.instanceCounter += 1;
        OgmaObject.instances.push(this);
    }

    get hash(): string {
        return this.hashString;
    }

    toString(): string {
        return "<OgmaObject>";
    }

    compareTo(other: OgmaObject): number {
        if (this === other) return 0;
        return this.hashString.localeCompare(other.hashString);
    }
}

class OgmaLinkedListElement extends OgmaObject {
    next: OgmaLinkedListElement;

    constructor(sib?) {
        super();
        this.next = (sib === undefined) ? null : sib;
    }
}

class OgmaLinkedList<T extends OgmaLinkedListElement> extends OgmaObject {
    head: T;
    tail: T;

    constructor() {
        super();
        this.head = null;
        this.tail = null;
    }

    toString(): string {
        let elementsString: string = undefined;
        let current: T = this.head;

        while (current !== null) {
            if (elementsString === undefined) {
                elementsString = current.toString();
            } else {
                elementsString += ", " + current.toString();
            }

            current = current.next as T;
        }

        if (elementsString === undefined)
            return "()";
        else
            return "(" + elementsString + ")";
    }

    push(element: T): OgmaLinkedList<T> {
        if ((this.head == null) || (this.tail == null)) {
            this.head = element;
            this.tail = element;
            element.next = null;
        } else  {
            element.next = this.head;
            this.head = element;
        }

        return this;
    }

    pop():T {
        let result: T;

        if ((this.head == null) || (this.tail == null)) return null;

        if (this.head === this.tail) {
            result = this.head;
            this.head = null;
            this.tail = null;
            result.next = null;
            return result;
        }

        result = this.head;
        this.head = this.head.next as T;

        return result;
    }

    butFirst(): OgmaLinkedList<T> {
        this.pop();
        return this;
    }

    add(element: T): OgmaLinkedList<T> {
        if ((this.head == null) || (this.tail == null)) {
            this.head = element;
            this.tail = element;
            element.next = null;
        } else {
            this.tail.next = element;
            element.next = null;
            this.tail = element;
        }

        return this;
    }

    insertAfter(memberElement: T, elementToAdd: T):  OgmaLinkedList<T> {
        if ((this.head == null) || (this.tail == null)) return null;

        if (this.head === memberElement) {
            if (this.tail === this.head) {
                this.head.next = elementToAdd;
                elementToAdd.next = null;
                this.tail = elementToAdd;
                return this;
            }
        }
        if (this.tail === memberElement) {
            elementToAdd.next = memberElement.next;
            memberElement.next = elementToAdd;
            this.tail =  elementToAdd;
            return this;
        }

        elementToAdd.next = memberElement.next;
        memberElement.next = elementToAdd;

        return this;
    }

    protected findPrecedesTail(): T {
        // Use only if head and tail are neither null nor the same;

        let current: T = this.head;

        while (current.next !== this.tail)
            current = current.next as T;

        return current;
    }

    removeLast(): T {
        let element: T;
        let before: T;

        if ((this.head === null) || (this.tail === null)) {
            return null;
        }
        if (this.head === this.tail) {
           element = this.head;
           this.head = null;
           this.tail = null;
           return element;
        }

        before = this.findPrecedesTail();
        element = before.next as T;
        before.next = null;
        this.tail = before;

        return element;
    }

    remove(element: T): OgmaLinkedList<T> {
        let current: T;
        let previous: T;

        if ((this.head === null) || (this.tail === null)) {
            return this;
        }
        if ((this.head === element) && (this.head === this.tail)) {
            this.head = null;
            this.tail = null;
            return this;
        }

        previous = this.head;
        current = this.head.next as T;

        while (current !== null) {
            if (current === element) {
                previous.next = current.next;
                current.next = null;

                if (this.tail === current) {
                    this.tail = previous;
                }

                return this;
            }

            current = current.next as T;
            previous = current;
        }

        return this;
    }

    length(): number {
        if ((this.head === null) || (this.tail === null)) return 0;

        let count: number = 0;
        let current: T = this.head;

        while (current !== null) {
            count += 1;
            current = current.next as T;
        }

        return count;
    }

    nth(i: number): T {
        if ((this.head === null) || (this.tail === null)) return null;

        let count: number = 0;
        let current: T = this.head;

        while (current !== null) {
            if (count == i) return current;

            count += 1;
            current = current.next as T;
        }

        return null;
    }

    isMember(something: T): boolean {
        let current: T = this.head;

        while (current !== null) {
            if (current === something)
                return true;
            current = current.next as T;
        }

        return false;
    }

    isEmpty(): boolean {
        return this.head === null;
    }

    first(): T {
        if (this.head === null) return null;
        return this.head;
    }

    last(): T {
        if (this.tail === null) return null;
        return this.tail;
    }
}

class OgmaDoubleLinkedListElement extends OgmaLinkedListElement {
    previous: OgmaDoubleLinkedListElement;
    parent: OgmaDoubleLinkedList<OgmaDoubleLinkedListElement>;

    constructor(parent?, leftSib?, rightSib?) {
        super(rightSib);
        this.parent = parent;
        this.previous = (leftSib === undefined) ? null : leftSib;
    }

    parentChanged(): void {

    }
}

class OgmaDoubleLinkedList<T extends OgmaDoubleLinkedListElement> extends OgmaDoubleLinkedListElement {
    head: T;
    tail: T;

    constructor() {
        super();
        this.head = null;
        this.tail = null;
    }

    toString(): string {
        let elementsString: string = undefined;
        let current: T = this.head;

        while (current !== null) {
            if (elementsString === undefined) {
                elementsString = current.toString();
            } else {
                elementsString += ", " + current.toString();
            }

            current = current.next as T;
        }

        if (elementsString === undefined)
            return "()";
        else
            return "(" + elementsString + ")";
    }

    public informMembersOfChange(): void {
        let current: OgmaDoubleLinkedListElement = this.head;

        while (current != null) {
            current.parentChanged();
            current = current.next as OgmaDoubleLinkedListElement;
        }
    }

    push(element: T): OgmaDoubleLinkedList<T> {
        element.parent = this;

        if ((this.head == null) || (this.tail == null)) {
            this.head = element;
            this.tail = element;
            element.next = null;
            element.previous = null;
        } else  {
            this.head.previous = element;
            element.next = this.head;
            element.previous = null;
            this.head = element;
        }

        this.informMembersOfChange();

        return this;
    }

    pop():T {
        let element: T;

        if ((this.head == null) || (this.tail == null)) return null;

        if (this.head === this.tail) {
            element = this.head;
            this.head = null;
            this.tail = null;
        } else {
            element = this.head;
            (element.next as T).previous = null;
            this.head = this.head.next as T;
        }

        element.parent = null;
        element.next = null;
        element.previous = null;

        this.informMembersOfChange();
        element.parentChanged();

        return element;
    }

    butFirst(): OgmaDoubleLinkedList<T> {
        this.pop();
        return this;
    }

    add(element: T): OgmaDoubleLinkedList<T> {
        element.parent = this;

        if ((this.head == null) || (this.tail == null)) {
            this.head = element;
            this.tail = element;
            element.next = null;
            element.previous = null;
        } else {
            element.previous = this.tail;
            this.tail.next = element;
            element.next = null;
            this.tail = element;
        }

        this.informMembersOfChange();

        return this;
    }

    insertAfter(memberElement: T, elementToAdd: T):  OgmaDoubleLinkedList<T> {
        if (memberElement.parent !== this) return this;
        if (elementToAdd.parent === this) return this;
        if ((this.head == null) || (this.tail == null)) return null;

        elementToAdd.parent = this;

        if (this.head === memberElement) {
            if (this.tail === this.head) {
                elementToAdd.previous = memberElement;
                memberElement.next = elementToAdd;
                elementToAdd.next = null;
                this.tail = elementToAdd;

                this.informMembersOfChange();

                return this;
            }
        }

        if (this.tail === memberElement) {
            elementToAdd.previous = this.tail;
            elementToAdd.next = null;
            memberElement.next = elementToAdd;
            this.tail =  elementToAdd;

            this.informMembersOfChange();

            return this;
        }

        elementToAdd.previous = memberElement;
        (memberElement.next as T).previous = elementToAdd;
        elementToAdd.next = memberElement.next;
        memberElement.next = elementToAdd;

        this.informMembersOfChange();

        return this;
    }

    remove(element: T): OgmaLinkedList<T> {
        if (element.parent === this) {
            if (this.head === element) {
                if (this.tail === element) {
                    this.head = null;
                    this.tail = null;
                } else {
                    this.head = element.next as T;
                    this.head.previous = null;
                }
            } else if (this.tail === element) {
                this.tail = element.previous as T;
                this.tail.next = null;
            } else {
                element.previous.next = element.next;
                (element.next as T).previous = element.previous;
            }

            element.parent = null;
            element.next = null;
            element.previous = null;

            this.informMembersOfChange();
        }

        // @ts-ignore
        return this;
    }

    removeLast(): T {
        let element: T;
        let before: T;

        if ((this.head === null) || (this.tail === null)) {
            return null;
        }

        element.parent = null;

        if (this.head === this.tail) {
            element = this.head;
            this.head = null;
            this.tail = null;
        } else {
            before = this.tail.previous as T;
            element = this.tail;
            element.previous = null;
            element.next = null;
            before.next = null;
            this.tail = before;
        }

        this.informMembersOfChange();
        element.parentChanged();

        return element;
    }

    length(): number {
        if ((this.head === null) || (this.tail === null)) return 0;

        let count: number = 0;
        let current: T = this.head;

        while (current !== null) {
            count += 1;
            current = current.next as T;
        }

        return count;
    }

    nth(i: number): T {
        if ((this.head === null) || (this.tail === null)) return null;

        let count: number = 0;
        let current: T = this.head;

        while (current !== null) {
            if (count == i) return current;

            count += 1;
            current = current.next as T;
        }

        return null;
    }

    isMember(something: T): boolean {
        let current: T = this.head;

        while (current !== null) {
            if (current === something)
                return true;
            current = current.next as T;
        }

        return false;
    }

    isEmpty(): boolean {
        return this.head === null;
    }

    first(): T {
        if (this.head === null) return null;
        return this.head;
    }

    last(): T {
        if (this.tail === null) return null;
        return this.tail;
    }

    sort(c: Comparator<T>): void {
        let len: number = this.length();

        if (len == 0) return;

        let current: T = this.head;
        let array: Array<T> = new Array<T>();

        while (current !== null) {
            array.push(current);
            current = current.next as T;
        }

        array.sort(function (a: T, b: T) {
            return c.compareTo(a, b);
        });

        for (let i: number = 0; i < len; i++) {
            if (i == 0) {
                array[i].previous = null;

                if ((i + 1) < len) {
                    array[i].next = array[i + 1];
                } else {
                    array[i].next = null;
                }
            } else if (i == (len - 1)) {
                array[i].previous = array[i - 1];
                array[i].next = null;
            } else {
                array[i].previous = array[i - 1];
                array[i].next = array[i + 1];
            }
        }

        this.head = array[0];
        this.tail = array[len - 1];

        for (let i: number = 0; i < len; i++) {
            array[i].parentChanged();
        }
    }
}

interface Collection<T> {
    clear(): void;
    size(): number;
    add(e: T): boolean;
    addAll(c: Collection<T>): boolean;
    addAllFromArray(a: Array<T>): boolean;
    remove(e: T): boolean;
    removeAll(c: Collection<T>): boolean;
    removeAllFromArray(a: Array<T>): boolean;
    contains(e: T): boolean;
    containsAll(c: Collection<T>): boolean;
    isEmpty(): boolean;
    toArray(): Array<T>;
}

interface List<T> extends Collection<T> {
    addAt(i: number, e: T): void;
    removeAt(i: number): T;
    insertBefore(newE: T, e: T): T
    get(i: number): T;
    set(i: number, value: T): void;
}

interface Set<T> extends Collection<T> {
    retainAll(c: Collection<T>): boolean;
    removeRandomElement(): T;
}

interface FastMap<K extends Object, V extends OgmaObject> {
    get(key: K): V;
    put(key: K, value: V): V;
}

interface Map<K extends Object, V extends OgmaObject> extends FastMap<K, V> {
    clear(): void;
    containsKey(key: K): boolean;
    containsValue(value: V): boolean;
    remove(key: K, value?: V): V;
    keySet(): Set<K>;
    values(): Collection<V>;
    getOrDefault(key: K, value: V): V;
}

class FastHashMap<K extends Object, V extends OgmaObject> extends OgmaObject implements FastMap<K, V> {
    private internalHash: Object;
    private keys: Array<string>;

    constructor () {
        super();
        this.internalHash = new Object();
    }

    private getHash(obj: K): string {
        if (typeof obj == "string") {
            return obj as string;
        } else if (typeof obj == "number") {
            return "" + obj;
        } else if (obj instanceof OgmaObject) {
            return obj.hash;
        } else {
            return null;
        }
    }

    toString(): string {
        let self: FastHashMap<K, V> = this;
        let result: string = undefined;

        this.keys.forEach(function (key: string) {
            let val: string = self.internalHash[key].toString();
            let valAsString: string = (val == null) ? "null" : val.toString();

            if (result === undefined) {
                result = key + " => " + valAsString;
            } else {
                result += ", ";
                result += key + " => " + valAsString;
            }
        });

        if (result === undefined) result = "";

        return "[" + result + "]";
    }

    get(key: K): V {
        if (typeof key == "string")
            return this.internalHash[key as string] as V;
        if (key instanceof OgmaObject) {
            // @ts-ignore
            return this.internalHash[(key as OgmaObject).hash];
        }
        return null;
    }

    put(key: K, value: V): V {
        let keyAsString: string;

        if (typeof key == "string")
            keyAsString = key as string;
        else {
            // @ts-ignore
            keyAsString = (key as OgmaObject).hash;
        }

        let oldValue: V = this.internalHash[keyAsString];

        this.internalHash[keyAsString] = value;

        return oldValue;
    }

}

class OgmaValueCounter {
    count: number;
    value: Object;

    constructor(value?: Object) {
        this.count = 1;
        this.value = value;
    }

    isZero(): boolean {
        if (this.count == 0) return true;
        return false;
    }

    hasValue(value: Object): boolean {
        return this.value == value;
    }

    incrementCount(): void {
        this.count += 1;
    }

    decrementCount(): void {
        this.count -= 1;
    }
}

class OgmaValuesCounter {
    valueCounters: Array<OgmaValueCounter>;

    constructor() {
        this.valueCounters = new Array<OgmaValueCounter>();
    }

    allValues(): Array<Object> {
        let result: Array<Object> = new Array<Object>();

        this.valueCounters.forEach(function (vc) {
            result.push(vc.value);
        });

        return result;
    }

    containsValue(value: Object): boolean {
        let vCount: number = this.valueCounters.length;

        for (let i: number = 0; i < vCount; i++) {
            if (this.valueCounters[i].value == value) {
                return true;
            }
        }

        return false;
    }

    addToCount(value: Object, inc: number): void {
        let vCount: number = this.valueCounters.length;

        for (let i: number = 0; i < vCount; i++) {
            if (this.valueCounters[i].value == value) {
                if (inc > 0)
                    this.valueCounters[i].incrementCount();
                else if (inc < 0) {
                    this.valueCounters[i].decrementCount();

                    if (this.valueCounters[i].isZero()) {
                        this.valueCounters.splice(i, 1);
                    }
                }
                return;
            }
        }

        this.valueCounters.push(new OgmaValueCounter(value));
    }
}

class HashMap<K extends Object, V extends OgmaObject> extends OgmaObject implements Map<K, V> {
    private internalMap: Object;
    private keys: Array<K>;
    private vals: OgmaValuesCounter;

    constructor() {
        super();
        this.internalMap = new Object();
        this.keys = new Array<K>();
        this.vals = new OgmaValuesCounter();
    }

    toString(): string {
        let self: HashMap<K, V> = this;
        let result: string = undefined;

        this.keys.forEach(function (key) {
            let val: V = self.get(key);
            let valAsString: string = (val == null) ? "null" : val.toString();

            if (result === undefined) {
                result = ("" + key) + " => " + valAsString;
            } else {
                result += ", ";
                result += ("" + key) + " => " + valAsString;
            }
        });

        if (result === undefined) result = "";

        return "[" + result + "]";
    }

    clear(): void {
        this.internalMap = new Object();
        this.keys = new Array<K>();
        this.vals = new OgmaValuesCounter();
    }

    private getHash(obj: K): string {
        if (typeof obj == "string") {
            return obj as string;
        } else if (typeof obj == "number") {
            return "" + obj;
        } else if (obj instanceof OgmaObject) {
            return obj.hash;
        } else {
            return null;
        }
    }

    private indexOfKey(key: K): number {
        let keyCount: number = this.keys.length;

        for (let i: number = 0; i < keyCount; i++) {
            let keyInKeys: Object = this.keys[i];

            if (keyInKeys == key) {
                return i;
            } else if ((typeof key == "string") && (typeof keyInKeys == "string")) {
                if ((key as string).localeCompare(keyInKeys as string) == 0)
                    return i;
            }
        }

        return -1;
    }

    containsKey(key: K): boolean {
        let keyCount: number = this.keys.length;

        for (let i: number = 0; i < keyCount; i++) {
            let keyInKeys: Object = this.keys[i];

            if (keyInKeys == key) {
                return true;
            } else if ((typeof key == "string") && (typeof keyInKeys == "string")) {
                if ((key as string).localeCompare(keyInKeys as string) == 0)
                    return true;
            }
        }

        return false;
    }

    containsValue(value: V): boolean {
        return this.vals.containsValue(value);
    }

    get(key: K): V {
        let hash: string = this.getHash(key);

        if (hash == null) return null;

        let val: Object = this.internalMap[hash];

        if (val == undefined) return null;

        if (val instanceof OgmaObject) return val as V;

        return null;
    }

    put(key: K, value: V): V {
        let hash: string = this.getHash(key);

        if (hash == null) return undefined;

        let oldValue: V = this.get(key);

        if (oldValue == null) {
            this.vals.addToCount(value, 1);
        } else {
            this.vals.addToCount(oldValue, -1);
        }

        if (this.keys.indexOf(key) == -1) {
            this.keys.push(key);
        }

        this.internalMap[hash] = value;
    }

    remove(key: K, value?: V): V {
        let oldValue: V = this.get(key);

        if (oldValue == null) return null;

        if (value !== undefined) {
            if (oldValue !== value) {
                return oldValue;
            }
        }

        let indexOfKey = this.indexOfKey(key);

        if (indexOfKey !== -1) {
            this.keys.splice(indexOfKey, 1);
            this.vals.addToCount(oldValue, -1);
            this.internalMap[this.getHash(key)] = undefined;
        }

        return oldValue;
    }

    keySet(): Set<K> {
        let result: LinearSet<K> = new LinearSet<K>();

        result.addAllFromArray(this.keys);

        return result;
    }

    values(): Collection<V> {
        let vals: Array<Object> = this.vals.allValues();
        let result: ArrayList<V> = new ArrayList<V>();

        vals.forEach(function(v:Object) {
            result.add(v as V);
        });

        return result;
    }

    getOrDefault(key: K, value: V): V {
        let oldValue: V = this.get(key);

        if (oldValue == null) return value;

        return oldValue;
    }
}

class LinearSet<T> extends OgmaObject implements Set<T> {
    internalArray: Array<T>;

    constructor () {
        super();
        this.internalArray = new Array<T>();
    }

    toString(): string {
        let result: string = undefined;
        let count: number = this.internalArray.length;
        let e: Object;
        let eAsString: string;

        for (let i: number = 0; i < count; i++) {
            e = this.internalArray[i];
            eAsString = (e instanceof OgmaObject) ? (e as OgmaObject).toString() : ("" + e);

            if (result === undefined) {
                result = eAsString;
            } else {
                result += (", " + eAsString);
            }
        }

        if (result === undefined) result = "";

        return "{" + result + "}";
    }

    clear(): void {
        this.internalArray = new Array<T>();
    }

    size(): number {
        return this.internalArray.length;
    }

    add(e: T): boolean {
        if (ogmaIndexOf(this.internalArray, e) !== -1) return false;
        this.internalArray.push(e);
        return true;
    }

    addAll(c: Collection<T>): boolean {
        let self:LinearSet<T> = this;
        let cAsArray: Array<T> = c.toArray();
        let addedNewMember: boolean = false;

        cAsArray.forEach(function (e:T) {
            if (ogmaIndexOf(self.internalArray, e) == -1) {
                self.internalArray.push(e);
                addedNewMember = true;
            }
        });

        return addedNewMember;
    }

    addAllFromArray(a: Array<T>): boolean {
        let self:LinearSet<T> = this;
        let addedNewMember: boolean = false;

        a.forEach(function (e:T) {
            if (ogmaIndexOf(self.internalArray, e) == -1) {
                self.internalArray.push(e);
                addedNewMember = true;
            }
        });

        return addedNewMember;
    }

    remove(e: T): boolean {
        let index: number = ogmaIndexOf(this.internalArray, e);

        if (index == -1) return false;

        this.internalArray.splice(index, 1);

        return true;
    }

    removeAll(c: Collection<T>): boolean {
        let newArray = new Array<T>();
        let len = this.internalArray.length;
        let removedAny: boolean = false;

        for (let i:number = 0; i < len; i++) {
            let e: T = this.internalArray[i];
            if (! c.contains(e)) {
                newArray.push(e);
            } else {
                removedAny = true;
            }
        }

        this.internalArray = newArray;

        return removedAny;
    }

    removeAllFromArray(a: Array<T>): boolean {
        let newArray = new Array<T>();
        let len = this.internalArray.length;
        let removedAny: boolean = false;

        for (let i:number = 0; i < len; i++) {
            let e: T = this.internalArray[i];
            if (ogmaIndexOf(a, e) == -1) {
                newArray.push(e);
            } else {
                removedAny = true;
            }
        }

        this.internalArray = newArray;

        return removedAny;
    }

    removeRandomElement(): T {
        if (this.internalArray.length == 0) return null;

        return this.internalArray.pop();
    }

    retainAll(c: Collection<T>): boolean {
        let cAsArray: Array<T> = c.toArray();
        let removedAny: boolean = false;
        let newArray: Array<T> = new Array();

        cAsArray.forEach(function (e:T) {
            if (ogmaIndexOf(this.internalArray, e) != -1) {
                newArray.push(e);
            }
        });

        removedAny = this.internalArray.length != newArray.length;

        this.internalArray = newArray;

        return removedAny;
    }

    contains(e: T): boolean {
        let index: number = ogmaIndexOf(this.internalArray, e);

        return (index != -1);
    }

    containsAll(c: Collection<T>): boolean {
        if (c === null) return true;

        let cAsArray: Array<T> = c.toArray();
        let len: number = c.size();

        for (let i: number = 0; i < len; i++) {
            if (! this.contains(cAsArray[i])) return false;
        }

        return true;
    }

    isEmpty(): boolean {
        return this.internalArray.length == 0;
    }

    toArray(): Array<T> {
        return [].concat(this.internalArray);
    }

}

class ArrayList<T> extends OgmaObject implements List<T> {
    internalArray: Array<T>;

    constructor () {
        super();
        this.internalArray = new Array<T>();
    }

    toString(): string {
        let result: string = undefined;
        let count: number = this.internalArray.length;
        let e: Object;
        let eAsString: string;

        for (let i: number = 0; i < count; i++) {
            e = this.internalArray[i];
            eAsString = (e instanceof OgmaObject) ? (e as OgmaObject).toString() : ("" + e);

            if (result === undefined) {
                result = eAsString;
            } else {
                result += (", " + eAsString);
            }
        }

        if (result === undefined) result = "";

        return "(" + result + ")";
    }

    clear(): void {
        this.internalArray = new Array<T>();
    }

    size(): number {
        return this.internalArray.length;
    }

    add(e: T): boolean {
        this.internalArray.push(e);
        return true;
    }

    addAll(c: Collection<T>): boolean {
        let cAsArray: Array<T> = c.toArray();
        this.internalArray = this.internalArray.concat(cAsArray);
        return true;
    }

    addAllFromArray(a: Array<T>): boolean {
        this.internalArray = this.internalArray.concat(a);
        return true;
    }

    addAt(i: number, e: T): void {
        this.internalArray.splice(i, 0, e);
    }

    remove(e: T): boolean {
        let index: number = this.internalArray.indexOf(e);

        if (index == -1) return false;

        this.internalArray.splice(index, 1);

        return true;
    }

    removeAll(c: Collection<T>): boolean {
        let newArray = new Array<T>();
        let len = this.internalArray.length;
        let removedAny: boolean = false;

        for (let i:number = 0; i < len; i++) {
            let e: T = this.internalArray[i];
            if (! c.contains(e)) {
                newArray.push(e);
            } else {
                removedAny = true;
            }
        }

        this.internalArray = newArray;

        return removedAny;
    }

    removeAllFromArray(a: Array<T>): boolean {
        let newArray = new Array<T>();
        let len = this.internalArray.length;
        let removedAny: boolean = false;

        for (let i:number = 0; i < len; i++) {
            let e: T = this.internalArray[i];
            if (ogmaIndexOf(a, e) == -1) {
                newArray.push(e);
            } else {
                removedAny = true;
            }
        }

        this.internalArray = newArray;

        return removedAny;
    }

    removeAt(i: number): T {
        let len = this.internalArray.length;

        if ((i < 0) || (i >= len)) return null;

        let element: T = this.internalArray[i];

        this.internalArray.splice(i, 1);

        return element;
    }

    insertBefore(newE: T, e: T): T {
        let len = this.internalArray.length;

        for (let i: number = 0; i < len; i++) {
            if (this.internalArray[i] == e) {
                this.internalArray.splice(i, 0, newE);
                return newE;
            }
        }

        return null;
    }

    get(i: number): T {
        return this.internalArray[i];
    }

    set(i: number, value: T): void {
        this.internalArray[i] = value;
    }

    contains(e: T): boolean {
        let index: number = this.internalArray.indexOf(e);

        return (index != -1);
    }

    containsAll(c: Collection<T>): boolean {
        if (c === null) return true;

        let len: number = c.size();
        let cAsArray: Array<T> = c.toArray();

        for (let i: number = 0; i < len; i++) {
            if (! this.contains(cAsArray[i])) return false;
        }

        return true;
    }

    isEmpty(): boolean {
        return this.internalArray.length == 0;
    }

    toArray(): Array<T> {
        return [].concat(this.internalArray);
    }
}

// TESTING CODE

class OgmaTestObject extends OgmaLinkedListElement {
    value: number;

    constructor(value: number) {
        super();
        this.value = value;
    }

    toString(): string {
        return "" + this.value;
    }
}

class OgmaTestObject2 extends OgmaDoubleLinkedListElement {
    value: number;

    constructor(parent?: OgmaDoubleLinkedList<OgmaTestObject2>, value?: number) {
        super(parent, null, null);
        this.value = value;
    }

    toString(): string {
        return "" + this.value;
    }

    parentChanged(): void {
        console.log("  Parent of " + this.toString() + " changed");
    }
}

class OgmaTestObject3 extends OgmaObject {
    name: string;

    constructor(name: string) {
        super();
        this.name = name;
    }

    toString() :string {
        return '|' + this.name + '|';
    }
}

function testOgmaPrimitives(): void {
    let numberList: OgmaLinkedList<OgmaTestObject> = new OgmaLinkedList<OgmaTestObject>();
    let numberList2: OgmaDoubleLinkedList<OgmaTestObject2> = new OgmaDoubleLinkedList<OgmaTestObject2>();
    let testElement: OgmaTestObject;
    let testElement2: OgmaTestObject2;
    let i: number;

    console.log("*Testing Linked Lists");

    for (i = 0; i < 10; i++) {
        console.log("Adding: " + i);
        numberList.add(new OgmaTestObject(i));
    }

    console.log("Added 0 through 9:");
    console.log(numberList.toString());

    console.log("Length is: " + numberList.length());

    console.log("Pushing -1:");
    numberList.push(new OgmaTestObject(-1));
    console.log(numberList.toString());

    console.log("Popping:");
    testElement =  numberList.pop();
    console.log("Value returned from pop:" + testElement.toString());
    console.log(numberList.toString());

    console.log("Inserting 2.5 after 2:");
    numberList.insertAfter(numberList.nth(2), new OgmaTestObject(2.5));
    console.log(numberList.toString());

    console.log("Popping all elements off of list:");

    while (! numberList.isEmpty()) {
        testElement = numberList.pop();
        console.log("Popped: " + testElement.toString());
    }

    console.log(numberList.toString());

    console.log("*Testing Double Linked Lists");

    for (i = 0; i < 10; i++) {
        console.log("Adding: " + i);
        numberList2.add(new OgmaTestObject2(null, i));
    }

    console.log("Added 0 through 9:");
    console.log(numberList2.toString());

    console.log("Length is: " + numberList2.length());

    console.log("Pushing -1:");
    numberList2.push(new OgmaTestObject2(null,-1));
    console.log(numberList2.toString());

    console.log("Popping:");
    testElement2 =  numberList2.pop();
    console.log("Value returned from pop:" + testElement2.toString());
    console.log(numberList2.toString());

    console.log("Inserting 2.5 after 2:");
    numberList2.insertAfter(numberList2.nth(2), new OgmaTestObject2(null,2.5));
    console.log(numberList2.toString());

    console.log("Popping all elements off of list:");

    while (! numberList2.isEmpty()) {
        testElement2 = numberList2.pop();
        console.log("Popped: " + testElement2.toString());
    }

    console.log(numberList2.toString());

    console.log("*Testing Array Lists");

    let list1:ArrayList<string> = new ArrayList<string>();

    console.log("Adding names of even numbers:");

    list1.add("two");
    list1.add("four");
    list1.add("six");
    list1.add("eight");
    list1.add("ten");

    console.log(list1.toString());

    console.log("Inserting names of odd numbers for each even number:");

    list1.insertBefore("one", "two");
    list1.insertBefore("three", "four");
    list1.insertBefore("five", "six");
    list1.insertBefore("seven", "eight");
    list1.insertBefore("nine", "ten");

    console.log(list1.toString());

    console.log("*Testing LinearSet");

    let set1:LinearSet<string> = new LinearSet<string>();

    set1.addAll(list1);

    console.log(set1.toString());

    console.log("Adding list to set again.");

    set1.addAll(list1);

    console.log(set1.toString());

    console.log("Removing one, five, and ten:");

    set1.remove("one");
    set1.remove("five");
    set1.remove("ten");

    console.log(set1.toString());

    console.log('Testing set1.contains("four"):');

    if (set1.contains("four")) {
        console.log("yes");
    } else {
        console.log("no");
    }

    console.log('Testing set1.contains("five"):');

    if (set1.contains("five")) {
        console.log("yes");
    } else {
        console.log("no");
    }

    console.log("* Testing FastHashMap");

    let map1: FastHashMap<OgmaTestObject3, OgmaTestObject3> = new FastHashMap<OgmaTestObject3, OgmaTestObject3>();
    let mapObj1: OgmaTestObject3 = new OgmaTestObject3("god");
    let mapObj2: OgmaTestObject3 = new OgmaTestObject3("dog");
    let mapObj3: OgmaTestObject3 = new OgmaTestObject3("not");
    let mapObj4: OgmaTestObject3 = new OgmaTestObject3("ton");

    map1.put(mapObj1, mapObj2);
    map1.put(mapObj2, mapObj1);

    map1.put(mapObj3, mapObj4);
    map1.put(mapObj4, mapObj3);

    let mapArray1: Array<OgmaTestObject3> = new Array<OgmaTestObject3>();

    mapArray1.push(mapObj1);
    mapArray1.push(mapObj2);
    mapArray1.push(mapObj3);
    mapArray1.push(mapObj4);

    mapArray1.forEach(function (e: OgmaTestObject3) {
        console.log(e.toString() + " => " + map1.get(e).toString());
    });

    console.log("* Testing FastHashMap");

    let map2: HashMap<OgmaTestObject3, OgmaTestObject3> = new HashMap<OgmaTestObject3, OgmaTestObject3>();

    map2.put(mapObj1, mapObj2);
    map2.put(mapObj2, mapObj1);

    map2.put(mapObj3, mapObj4);
    map2.put(mapObj4, mapObj3);

    mapArray1.forEach(function (e: OgmaTestObject3) {
        console.log(e.toString() + " => " + map2.get(e).toString());
    });

    console.log("map2.keySet()=" + map2.keySet());
    console.log("map2.keySet().size()=" + map2.keySet().size());

    console.log("map2.values()=" + map2.values());
    console.log("map2.values().size()=" + map2.values().size());
}
