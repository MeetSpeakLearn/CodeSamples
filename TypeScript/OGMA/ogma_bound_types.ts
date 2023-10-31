
enum HTMLElementType {
    UNKNOWN,
    INLINE,
    BLOCK_LEVEL
}

enum DropEffect {
    COPY = "copy",
    MOVE = "move",
    LINK = "link",
    NONE = "none",
    UNKNOWN = "unknown"
}

enum EffectAllowed {
    NONE = "none",
    COPY = "copy",
    COPY_LINK = "copyLink",
    COPY_MOVE = "copyMove",
    LINK = "link",
    LINK_MOVE = "linkMove",
    MOVE = "move",
    ALL = "all",
    UNINITIALIZED = "uninitialized",
    UNKNOWN = "unknown"
}

enum DataTransferItemKind {
    STRING = "string",
    FILE = "file",
    UNKNOWN = "unknown"
}



function findOgmaObject(id: string): any {
    if ((id !== undefined) && (id !== null)) {
        if (id.length > 0) {
            if (id.charCodeAt(0) == OgmaHTMLEvent._ogmaPrefix) {
                let ogmaObj: OgmaObject = OgmaObject.get(id);

                if (ogmaObj instanceof OgmaHTMLNode) {
                    return ogmaObj.ogmaParent;
                } else {
                    return ogmaObj;
                }
            }
        }
    }

    return null;
}

class OgmaHTMLDataTransferItem {
    _htmlDataTransferItem: DataTransferItem;

    constructor(item: DataTransferItem) {
        this._htmlDataTransferItem = item;
    }

    get kind(): DataTransferItemKind {
        switch (this._htmlDataTransferItem.kind) {
            case "string": return DataTransferItemKind.STRING;
            case "file": return DataTransferItemKind.FILE;
            default: return DataTransferItemKind.UNKNOWN;
        }
    }

    get type(): string {
        return this._htmlDataTransferItem.type;
    }

    get ogmaObject(): OgmaObject {
        let id: string = this.id;

        if (id !== null) {
            return findOgmaObject(id);
        }

        return null;
    }

    get id(): string {
        let id: string = null;

        this._htmlDataTransferItem.getAsString(function (s: string) {
            id = s;
        });

        return id;
    }

}

class OgmaHTMLDataTransfer {
    _dataTransfer: DataTransfer;

    constructor(dataTransfer: DataTransfer) {
        this._dataTransfer = dataTransfer;
    }

    get dropEffect(): DropEffect {
        let de: string = this._dataTransfer.dropEffect;

        switch (de) {
            case "copy": return DropEffect.COPY;
            case "move": return DropEffect.MOVE;
            case "link": return DropEffect.LINK;
            case "none": return DropEffect.NONE;
        }

        return DropEffect.UNKNOWN;
    }

    set dropEffect(dropEffect: DropEffect) {
        this._dataTransfer.dropEffect = dropEffect;
    }

    get effectAllowed(): EffectAllowed {
        let ea: string = this._dataTransfer.effectAllowed;

        switch (ea) {
            case "none": return EffectAllowed.NONE;
            case "copy": return EffectAllowed.COPY;
            case "copyLink": return EffectAllowed.COPY_LINK;
            case "copyMove": return EffectAllowed.COPY_MOVE;
            case "link": return EffectAllowed.LINK;
            case "linkMove": return EffectAllowed.LINK_MOVE;
            case "move": return EffectAllowed.MOVE;
            case "all": return EffectAllowed.ALL;
            case "uninitialized": return EffectAllowed.UNINITIALIZED;
        }

        return EffectAllowed.UNKNOWN;
    }

    set effectAllowed(effectAllowed: EffectAllowed) {
        this._dataTransfer.effectAllowed = effectAllowed;
    }

    get files(): Array<string> {
        let listOfValues: Array<string> = new Array<string>();

        for (let i: number = 0; i < this._dataTransfer.files.length; i++) {
            listOfValues.push(this._dataTransfer.files.item(i).name);
        }

        return listOfValues;
    }

    get ogmaItems(): Array<OgmaObject> {
        let ogmaItems: Array<OgmaObject> = new Array<OgmaObject>();
        let count: number = this._dataTransfer.items.length;

        for (let i: number = 0; i < count; i++) {
            this._dataTransfer.items.item(i).getAsString(function (id:string) {
                let ogmaObject: OgmaObject = findOgmaObject(id);

                if (ogmaObject !== null) ogmaItems.push(ogmaObject);
            })
        }

        return ogmaItems;
    }

    get items(): Array<OgmaHTMLDataTransferItem> {
        let items: Array<OgmaHTMLDataTransferItem> = new Array<OgmaHTMLDataTransferItem>();
        let count: number = this._dataTransfer.items.length;

        for (let i: number = 0; i < count; i++) {
            items.push(new OgmaHTMLDataTransferItem(this._dataTransfer.items[i]));
        }

        return items;
    }

    get ogmaSource(): OgmaObject {
        let ogmaID: string = this._dataTransfer.getData(OgmaObject.MIME_TYPE_SOURCE);

        if ((ogmaID === undefined) || (ogmaID === null))
            return null;

        if (ogmaID == "")
            return null;

        return findOgmaObject(ogmaID);
    }

    set ogmaDestination(ogmaObj: OgmaObject) {
        this._dataTransfer.setData(OgmaObject.MIME_TYPE_DESTINATION, ogmaObj.hash);
    }

    get ogmaDestination(): OgmaObject {
        let ogmaID: string = this._dataTransfer.getData(OgmaObject.MIME_TYPE_DESTINATION);

        if ((ogmaID === undefined) || (ogmaID === null))
            return null;

        if (ogmaID == "")
            return null;

        return findOgmaObject(ogmaID);
    }

    set ogmaSource(ogmaObj: OgmaObject) {
        console.log("ogmaSource=" + ogmaObj + ", ogmaObj.hash=" + ogmaObj.hash);
        this._dataTransfer.setData(OgmaObject.MIME_TYPE_SOURCE, ogmaObj.hash);
    }

    get types(): Array<string> {
        let types: Array<string> = new Array<string>();
        let count: number = this._dataTransfer.types.length;

        for (let i: number = 0; i < count; i++)
            types.push(this._dataTransfer.types[i]);

        return types;
    }

    clearData(format?: string): void {
        this._dataTransfer.clearData(format);
    }

    getData(format: string): string {
        return this._dataTransfer.getData(format);
    }

    setData(format: string, data: string): void {
        this._dataTransfer.setData(format, data);
    }

    getOgmaData(format: string): OgmaObject {
        let data: string = this._dataTransfer.getData(format);
        return findOgmaObject(data);
    }

    setOgmaData(format: string, data: OgmaObject) {
        this._dataTransfer.setData(format, data.hash);
    }

    setDragImage(something: any, xOffset: number, yOffset: number): void {
        if (something instanceof Element) {
            this._dataTransfer.setDragImage(something as Element, xOffset, yOffset);
        } else if (something instanceof OgmaDomElement) {
            this._dataTransfer.setDragImage((something as OgmaDomElement).element, xOffset, yOffset);
        } else if (something instanceof OgmaHTMLElement) {
            this._dataTransfer.setDragImage((something as OgmaHTMLElement).htmlElement, xOffset, yOffset);
        }
    }
}

class OgmaHTMLEvent {
    static _ogmaPrefix: number = "ω".charCodeAt(0);

    _event: any;

    // e is an instant of the DOM type Event.
    constructor(e: any) {
        this._event = e;
    }

    get bubbles(): boolean {
        return this._event.bubbles;
    }

    get cancelable(): boolean {
        return this._event.cancelable;
    }

    set cancelBubble(v: boolean) {
        this._event.cancelBubble = v;
    }

    get composed(): boolean {
        return this._event.composed;
    }

    get currentTarget(): any {
        let et: any = this._event.currentTarget;
        let id: string;

        if (et instanceof HTMLDivElement) {
            id = et.id;
            let ogmaObject: any = findOgmaObject(id);

            if (ogmaObject !== null)
                return ogmaObject;
        }

        return et;
    }

    get currentRawTarget(): any {
        return this._event.currentTarget;
    }

    get defaultPrevented(): boolean {
        return this._event.defaultPrevented;
    }

    get eventPhase(): boolean {
        return this._event.eventPhase;
    }

    get isTrusted(): boolean {
        return this._event.isTrusted;
    }

    get returnValue(): boolean {
        return this._event.returnValue;
    }

    set returnValue(v: boolean) {
        this._event.returnValue = v;
    }

    get target(): any {
        let et: any = this._event.target;
        let id: string;

        if (et instanceof HTMLDivElement) {
            id = et.id;
            let ogmaObject: any = findOgmaObject(id);

            if (ogmaObject !== null)
                return ogmaObject;
        }

        return et;
    }

    get rawTarget(): any {
        return this._event.target;
    }

    get timeStamp(): any {
        return this._event.timeStamp;
    }

    get type(): string {
        return this._event.type;
    }
}

enum MouseButton {
    MAIN,
    AUXILIARY,
    SECONDARY,
    FOURTH,
    FIFTH,
    UNDEFINED
}

class OgmaHTMLMouseEvent extends OgmaHTMLEvent {

    // e is an instant of the DOM type Event.
    constructor(e: any) {
       super(e);
    }

    get altKey(): boolean {
        return this._event.altKey;
    }

    get button(): MouseButton {
        let buttonAsInt: number = this._event.button;

        switch (buttonAsInt) {
            case 0: return MouseButton.MAIN;
            case 1: return MouseButton.AUXILIARY;
            case 2: return MouseButton.SECONDARY;
            case 3: return MouseButton.FOURTH;
            case 4: return MouseButton.FIFTH;
        }

        return MouseButton.UNDEFINED;
    }

    get buttonsPressed(): Array<MouseButton> {
        let buttons:Array<MouseButton> = new Array<MouseButton>();
        let buttonsFlag: number = this._event.buttons;

        if ((buttonsFlag & 1) != 0) {
            buttons.push(MouseButton.MAIN);
        }

        if ((buttonsFlag & 2) != 0) {
            buttons.push(MouseButton.SECONDARY);
        }

        if ((buttonsFlag & 4) != 0) {
            buttons.push(MouseButton.AUXILIARY);
        }

        if ((buttonsFlag & 8) != 0) {
            buttons.push(MouseButton.FOURTH);
        }

        if ((buttonsFlag & 16) != 0) {
            buttons.push(MouseButton.FIFTH);
        }

        return buttons;
    }

    get clientX(): number {
        return this._event.clientX;
    }

    get clientY(): number {
        return this._event.clientY;
    }

    get ctrlKey (): boolean {
        return this._event.ctrlKey;
    }

    get metaKey  (): boolean {
        return this._event.metaKey ;
    }

    get pageX(): number {
        return this._event.pageX;
    }

    get pageY(): number {
        return this._event.pageY;
    }

    get region(): string {
        return this._event.region;
    }

    get relatedTarget(): any {
        let et: any = this._event.relatedTarget;
        let id: string;

        if (et instanceof HTMLDivElement) {
            id = et.id;
            let ogmaObject: any = findOgmaObject(id);

            if (ogmaObject !== null)
                return ogmaObject;
        }

        return et;
    }

    get relatedTargetRaw(): any {
        return this._event.relatedTarget;
    }

    get screenX(): number {
        return this._event.screenX;
    }

    get screenY(): number {
        return this._event.screenY;
    }

    get shiftKey (): boolean {
        return this._event.shiftKey;
    }
}

class OgmaHTMLDragEvent extends OgmaHTMLMouseEvent {
    constructor(e: any) {
        super(e);
    }

    get dataTransfer(): OgmaHTMLDataTransfer {
        return new OgmaHTMLDataTransfer(this._event.dataTransfer);
    }

    get ogmaSource(): OgmaObject {
        let dt: OgmaHTMLDataTransfer = this.dataTransfer;

        return dt.ogmaSource;
    }

    get ogmaDestination(): OgmaObject {
        let dt: OgmaHTMLDataTransfer = this.dataTransfer;

        return dt.ogmaDestination;
    }
}

class OgmaHTMLObject extends OgmaObject {
    protected _htmlNode: Node;
    protected _parent: OgmaHTMLObject = null;
    protected _children: ArrayList<OgmaHTMLObject> = new ArrayList<OgmaHTMLObject>();
    protected _type: HTMLElementType = HTMLElementType.UNKNOWN;

    constructor(id?:string) {
        super();

        if (id === undefined) return;

        this._htmlNode = document.getElementById(id);
    }

    get htmlNode(): Node {
        return this._htmlNode;
    }

    set htmlNode(newNode: Node) {
        this._htmlNode = newNode;
    }

    get parent(): OgmaHTMLObject {
        return this._parent;
    }

    set parent(newParent: OgmaHTMLObject) {

        if ((this._parent !== undefined) && (this._parent !== null)) {
            if (this._parent === newParent)
                return;
            else if ((this._parent.children !== undefined) && (this._parent.children !== null)){
                this._parent.children.remove(this);
            }
        }

        newParent.appendChild(this);
    }

    get children(): ArrayList<OgmaHTMLObject> {
        if (this._type === HTMLElementType.INLINE) {
            alert("Attempt to get children of inline HTMLElement " + this.toString());
            return null;
        }

        return this._children;
    }

    appendChild(child: OgmaHTMLObject): OgmaHTMLObject {
        if (this._type === HTMLElementType.INLINE) {
            alert("Attempt to appendChild to inline HTMLElement " + this.toString());
            return null;
        }

        if (! this._children.contains(child)) {
            this._children.add(child);
            child.parent = this;
            this.htmlNode.appendChild(child.htmlNode);
        }

        return child;
    }

    removeChild(child: OgmaHTMLObject): OgmaHTMLObject {
        if (this._type === HTMLElementType.INLINE) {
            alert("Attempt to removeChild from inline HTMLElement " + this.toString());
            return null;
        }

        if (this._children.contains(child)) {
            this._children.remove(child);
            this.htmlNode.removeChild(child.htmlNode);
        }

        return child;
    }

    removeAllChildren(): void {
        let self: OgmaHTMLObject = this;

        this.children.toArray().forEach(function (child: OgmaHTMLObject) {
            self.removeChild(child);
        });
    }

    replaceChild(newChild: OgmaHTMLObject, oldChild: OgmaHTMLObject): OgmaHTMLObject {
        if (this._type === HTMLElementType.INLINE) {
            alert("Attempt to replaceChild of inline HTMLElement " + this.toString());
            return null;
        }

        if (this._children.contains(oldChild)) {
            this._children.remove(oldChild);

            if (! this._children.contains(newChild)) {
                this._children.add(newChild);
            }

            this.htmlNode.replaceChild(newChild.htmlNode, oldChild.htmlNode);
        }

        return newChild;
    }

    insertBefore(newNode: OgmaHTMLObject, existingNode: OgmaHTMLObject) : OgmaHTMLObject {
        if (this._type === HTMLElementType.INLINE) {
            alert("Attempt to insertBefore on inline HTMLElement " + this.toString());
            return null;
        }

        if (this._children.contains(existingNode)) {
            this._children.insertBefore(newNode, existingNode);
            newNode.parent = this;

            this.htmlNode.insertBefore(newNode.htmlNode, existingNode.htmlNode);
        }

        return newNode;
    }

    get domParent(): Node {
        if (this._htmlNode !== undefined)
            return this._htmlNode.parentElement;
        return null;
    }

    set domParent(newParent: Node)  {
        if (this._htmlNode !== undefined) {
            if ((this._htmlNode.parentElement !== undefined)
                && (this._htmlNode.parentElement !== null)) {
                this._htmlNode.parentElement.removeChild(this._htmlNode);
            }

            newParent.appendChild(this._htmlNode);
        }
    }

    get domParentElement(): HTMLElement {
        if (this._htmlNode !== undefined)
            return this._htmlNode.parentElement;
        return null;
    }

    set domParentElement(newParent: HTMLElement)  {
        if (this._htmlNode !== undefined) {
            if ((this._htmlNode.parentElement !== undefined)
                && (this._htmlNode.parentElement !== null)) {
                this._htmlNode.parentElement.removeChild(this._htmlNode);
            }

            newParent.appendChild(this._htmlNode);
        }
    }

    getAttributeValue(name: string): string {
        if (this._htmlNode === undefined) return undefined;
        (this._htmlNode as HTMLElement).getAttribute(name);
    }

    setAttributeValue(name: string, value:string): void {
        if (this._htmlNode === undefined) return;
        (this._htmlNode as HTMLElement).setAttribute(name, value);
    }
}

class OgmaHTMLNode extends OgmaHTMLObject {
    protected _ogmaParent: IOgmaHTMLBoundObject;
    protected _eventTypesOfInterest: LinearSet<string>;

    constructor (ogmaParent?: IOgmaHTMLBoundObject, htmlNode?: Node) {
        super();
        this._ogmaParent = ogmaParent;
        this._htmlNode = htmlNode;
        this._eventTypesOfInterest = new LinearSet<string>();
    }

    get ogmaParent(): IOgmaHTMLBoundObject {
        return (this._ogmaParent === undefined) ? null : this._ogmaParent;
    }

    set ogmaParent(newParent: IOgmaHTMLBoundObject) {
        this._ogmaParent = newParent;
    }

    registerInterestInEventType(eventType: string): void {
        let self: OgmaHTMLNode = this;

        this._eventTypesOfInterest.add(eventType);

        if (this._htmlNode !== undefined) {
            if (eventType.localeCompare("click") == 0) {
                this._htmlNode.addEventListener("click",function (event) {
                    if ((self._ogmaParent !== undefined) && (self._ogmaParent !== null)) {
                        event.stopPropagation();
                        self._ogmaParent.ogmaHTMLElementListener(self, "click", event);
                    }
                });
            } else if (eventType.localeCompare("mouseenter") == 0) {
                this._htmlNode.addEventListener("mouseenter",function (event) {
                    if ((self._ogmaParent !== undefined) && (self._ogmaParent !== null))
                        self._ogmaParent.ogmaHTMLElementListener(self, "mouseenter", event);
                });
            } else if (eventType.localeCompare("mouseleave") == 0) {
                this._htmlNode.addEventListener("mouseleave",function (event) {
                    if ((self._ogmaParent !== undefined) && (self._ogmaParent !== null))
                        self._ogmaParent.ogmaHTMLElementListener(self, "mouseleave", event);
                });
            }
        }
    }
}

class OgmaHTMLElement extends OgmaHTMLNode {
    protected _tagName: string;
    protected _htmlElement: HTMLElement;

    constructor(ogmaParent?: IOgmaHTMLBoundObject, tagName?: string, htmlElement?: HTMLElement) {
        super(ogmaParent);
        this._ogmaParent = ogmaParent;

        if (htmlElement !== undefined) {
            if (tagName !== undefined) {
                if (htmlElement.tagName.localeCompare(tagName) == 0) {
                    this._htmlElement = htmlElement;
                } else {
                    this._htmlElement = document.createElement(tagName);
                }
            } else {
                this._tagName = htmlElement.tagName;
                this._htmlElement = htmlElement;
            }
        } else if (tagName !== undefined) {
            this._tagName = tagName;
            this._htmlElement = document.createElement(tagName);
        } else {
            this._tagName = undefined;
            this._htmlElement = undefined;
        }

        if (this._htmlElement !== undefined) {
            this._htmlNode = this._htmlElement;
            this._htmlElement.id = this.hashString;
        }
    }

    toString(): string {
        let result: string = undefined;

        this._children.toArray().forEach(function (child: OgmaHTMLObject): void {
            if (result === undefined) {
                result = child.toString();
            } else {
                result += ", " + child.toString();
            }
        });

        if (result === undefined)
            result = "";

        return this.tagName + "[" + this._htmlElement.id + "]:(" + result + ")";
    }

    get id(): string {
        return this._htmlElement.id;
    }

    get tagName(): string {
        return this._tagName;
    }

    get htmlElement(): HTMLElement {
        return this._htmlElement;
    }

    protected broadcast(source: OgmaHTMLNode, eventType: string, data?: Object): void {
        let self: OgmaHTMLElement = this;

        if ((self._ogmaParent !== undefined) && (self._ogmaParent !== null)) {
            self._ogmaParent.ogmaHTMLElementListener(self, eventType, data);
        }
    }

    get text(): string {
        if (this._htmlElement !== undefined)
            return this._htmlElement.innerText;
        else
            return null;
    }

    set text(text: string) {
        if (this._htmlElement !== undefined)
            this._htmlElement.innerText = text;
    }

    get className() :string {
        if (this._htmlElement !== undefined)
            return this._htmlElement.className;
        else
            return null;
    }

    set className(name: string) {
        if (this._htmlElement !== undefined)
            this._htmlElement.className = name;
    }

    get classList():ArrayList<string> {
        if (this._htmlElement === undefined) return null;

        let result: ArrayList<string> = new ArrayList<string>();
        let len = this._htmlElement.classList.length;

        for (let i = 0; i < len; i++) {
            result.add(this._htmlElement.classList.item(i));
        }

        return result;
    }

    addClass(newClass: string): void {
        if (this._htmlElement === undefined) return;
        this._htmlElement.classList.add(newClass);
    }

    removeClass(newClass: string): void {
        if (this._htmlElement === undefined) return;
        this._htmlElement.classList.remove(newClass);
    }

    set foreground(name: string) {
        this._htmlElement.style.color = name;
    }

    set background(name: string) {
        this._htmlElement.style.backgroundColor = name;
    }

    set textDecoration(name: string) {
        this._htmlElement.style.textDecoration = name;
    }

    setTextDecorationNormal(): void {
        this._htmlElement.style.textDecoration = "none";
    }

    setTextDecorationLineThrough(): void {
        this._htmlElement.style.textDecoration = "line-through";
    }

    setTextDecorationUnderline(): void {
        this._htmlElement.style.textDecoration = "underline";
    }

    set fontWeight(name: string) {
        this._htmlElement.style.fontWeight = name;
    }

    setFontWeightNormal(): void {
        this._htmlElement.style.fontWeight = "normal";
    }

    setFontWeightBold(): void {
        this._htmlElement.style.fontWeight = "bold";
    }

    set fontStyle(name: string) {
        this._htmlElement.style.fontStyle = name;
    }

    setFontStyleNormal(): void {
        this._htmlElement.style.fontStyle = "normal";
    }

    setFontStyleItalic(): void {
        this._htmlElement.style.fontStyle = "italic";
    }

    setFontStyleOblique(): void {
        this._htmlElement.style.fontStyle = "oblique";
    }

    get display(): string {
        return this._htmlElement.style.display;
    }

    set display(value: string) {
        this._htmlElement.style.display = value;
    }
}

class OgmaHTMLSpanElement extends OgmaHTMLElement {
    constructor(ogmaParent?: IOgmaHTMLBoundObject, htmlElement?: HTMLSpanElement) {
        super(ogmaParent, "span", htmlElement);
        this.ogmaParent = ogmaParent;
        this._type = HTMLElementType.INLINE;
    }

    set ogmaParent(newParent: IOgmaHTMLBoundObject) {
        this._ogmaParent = newParent;
    }

    get textContent(): string {
        return  (this._htmlElement as HTMLSpanElement).textContent;
    }

    set textContent(text: string) {
        (this._htmlElement as HTMLSpanElement).textContent = text;
    }

    appendText(text: string) {
        (this._htmlElement as HTMLSpanElement).appendChild(document.createTextNode(text));
    }

    getTitle():string {
        return this.getAttributeValue("title");
    }

    setTitle(title:string): void {
        this.setAttributeValue("title", title);
    }
}

class OgmaHTMLDivElement extends OgmaHTMLElement {
    constructor(ogmaParent?: IOgmaHTMLBoundObject, htmlElement?: HTMLSpanElement) {
        super(ogmaParent, "div", htmlElement);
        this.ogmaParent = ogmaParent;
        this._type = HTMLElementType.BLOCK_LEVEL;
    }
}

class OgmaHTMLParagraphElement extends OgmaHTMLElement {
    constructor(ogmaParent?: IOgmaHTMLBoundObject, htmlElement?: HTMLSpanElement) {
        super(ogmaParent, "p", htmlElement);
        this.ogmaParent = ogmaParent;
        this._type = HTMLElementType.BLOCK_LEVEL;
    }
}

interface IOgmaHTMLBoundObject {
    addOgmaHTMLElement(e: OgmaHTMLElement): OgmaHTMLElement;
    removeOgmaHTMLElement(e: OgmaHTMLElement): void;
    clearOgmaHTMLElements(): void;
    ogmaHTMLElementListener(source: OgmaHTMLNode, eventType: string, data?: Object): void;
    getOgmaHTMLElement(tagName:string): OgmaHTMLElement;
}

class OgmaHTMLBoundObject extends OgmaObject implements IOgmaHTMLBoundObject {
    protected ogmaHTMLElements: ArrayList<OgmaHTMLElement>;

    constructor() {
        super();
        this.ogmaHTMLElements = new ArrayList<OgmaHTMLElement>();
    }

    addOgmaHTMLElement(e: OgmaHTMLElement): OgmaHTMLElement {
        this.ogmaHTMLElements.add(e);
        e.ogmaParent = this;
        return e;
    }

    removeOgmaHTMLElement(e: OgmaHTMLElement): void {
        this.ogmaHTMLElements.remove(e);
        e.ogmaParent = null;
    }

    clearOgmaHTMLElements(): void {
        let self: IOgmaHTMLBoundObject = this;
        let allElements: Array<OgmaHTMLElement> = [].concat(this.ogmaHTMLElements.toArray());

        allElements.forEach(function (e) {
            self.removeOgmaHTMLElement(e);
        });
    }

    ogmaHTMLElementListener(source: OgmaHTMLNode, eventType: string, data?: Object): void {
    }

    getOgmaHTMLElement(tagName:string): OgmaHTMLElement {
        let len: number = this.ogmaHTMLElements.size();
        let element: OgmaHTMLElement = null;
        let index: number;

        for (index = 0; index < len; index++) {
            element = this.ogmaHTMLElements.get(index);
            if (element.tagName.localeCompare(tagName) == 0) break;
        }

        return element;
    }
}

class OgmaHTMLBoundTestObject extends OgmaHTMLBoundObject {
    protected _lexeme: string;
    protected _myDiv: OgmaHTMLDivElement;
    protected _mySpan1: OgmaHTMLSpanElement;
    protected _mySpan2: OgmaHTMLSpanElement;

    constructor(lexeme: string) {
        super();

        this._myDiv = this.addOgmaHTMLElement(new OgmaHTMLDivElement(this)) as OgmaHTMLDivElement;

        this._mySpan1 = this.addOgmaHTMLElement(new OgmaHTMLSpanElement(this)) as OgmaHTMLSpanElement;
        this._mySpan1.registerInterestInEventType("click");
        this._mySpan1.registerInterestInEventType("mouseenter");
        this._mySpan1.registerInterestInEventType("mouseleave");

        this._mySpan2 = this.addOgmaHTMLElement(new OgmaHTMLSpanElement(this)) as OgmaHTMLSpanElement;
        this._mySpan2.registerInterestInEventType("click");
        this._mySpan2.registerInterestInEventType("mouseenter");
        this._mySpan2.registerInterestInEventType("mouseleave");

        this._myDiv.registerInterestInEventType("click");
        this._myDiv.registerInterestInEventType("mouseenter");
        this._myDiv.registerInterestInEventType("mouseleave");

        this._myDiv.appendChild(this._mySpan2);
        this._myDiv.appendChild(this._mySpan1);

        this.lexeme = lexeme;
        this._mySpan2.text = "the";
        this._mySpan2.appendText(" ");
    }

    get lexeme(): string {
        return this._lexeme;
    }

    set lexeme(newLex: string) {
        this._lexeme = newLex;
        this._mySpan1.text = newLex;
    }

    ogmaHTMLElementListener(source: OgmaHTMLNode, eventType: string, data?: Object): void {
        console.log("  received " + eventType + " event from " + source.toString());
    }
}

function testOgmaBoundTypes(): void {
    let test1: OgmaHTMLBoundTestObject = new OgmaHTMLBoundTestObject("stephanie");

    test1.getOgmaHTMLElement("div").domParent = document.getElementById("p1");
}
