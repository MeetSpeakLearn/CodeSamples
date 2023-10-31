
enum GrammaticalGender {
    UNKNOWN,
    MASCULINE,
    FEMININE,
    NEUTER,
    ANIMATE,
    INANIMATE,
    COMMON
}

enum GrammaticalNumber {
    NONE,
    SINGULAR,
    DUAL,
    TRIAL,
    QUADRAL,
    PLURAL,
    COLLECTIVE
}

enum EnglishPlurality {
    UNKNOWN_PLURALITY,
    SINGULAR_ONLY,
    PLURAL_ONLY,
    SINGULAR_OR_PLURAL
}

enum PartOfSpeechType {
    NONE,
    VERB,
    NOUN,
    PREPOSITION,
    ADVERB,
    ADJECTIVE,
    PRONOUN,
    CONJUNCTION,
    INTERJECTION,
}

enum AdjectivalOrderPriority {
    ARTICLE,
    DEMONSTRATIVE,
    DETERMINER,
    POSSESSIVE,
    QUANTIFIER,
    OPINION,
    SIZE,
    PHYSICAL_QUALITY,
    SHAPE,
    AGE,
    COLOR,
    ORIGIN,
    MATERIAL,
    TYPE,
    PURPOSE
}

enum PhraseType {
    UNKNOWN,
    VERB,
    NOUN,
    PREPOSITION,
    ADVERB,
    ADJECTIVE,
    INFINITIVE,
    GERUND,
    PARTICIPLE,
    ABSOLUTE
}

enum NounType {
    UNKNOWN,
    COMMON,
    PROPER,
    CONCRETE,
    ABSTRACT,
    COLLECTIVE
}

enum NounCountabilityType {
    COUNTABLE,
    UNCOUNTABLE
}

enum VerbType {
    MAIN,
    LINKING,
    AUXILIARY,
    MODAL,
    STATE_AND_ACTION
}

enum VerbTransitivity {
    Transitive,
    Intransitive
}

enum AdjectiveType {
    ARTICLE,
    DEMONSTRATIVE,
    DETERMINER,
    POSSESSIVE,
    QUANTIFIER,
    ADJECTIVE
}

enum AdverbType {
    UNKNOWN,
    TIME,
    MANNER,
    DEGREE,
    PLACE,
    FREQUENCY
}

enum PronounType {
    NONE,
    INFORMAL,
    FORMAL
}

enum EnglishLanguageTense {
    UNKNOWN,
    SIMPLE_PRESENT,
    PRESENT_PROGRESSIVE,
    SIMPLE_PAST,
    PAST_PROGRESSIVE,
    PRESENT_PERFECT_SIMPLE,
    PRESENT_PERFECT_PROGRESSIVE,
    PAST_PERFECT_SIMPLE,
    PAST_PERFECT_PROGRESSIVE,
    FUTURE_I_SIMPLE,
    FUTURE_I_PROGRESSIVE,
    FUTURE_II_SIMPLE,
    FUTURE_II_PROGRESSIVE,
    CONDITIONAL_I_SIMPLE,
    CONDITIONAL_I_PROGRESSIVE,
    CONDITIONAL_II_SIMPLE,
    CONDITIONAL_II_PROGRESSIVE
}

enum EnglishDeterminerType {
    UNKNOWN,
    PREDETERMINER,
    CENTRAL_DETERMINER,
    POST_DETERMINER
}

class OgmaConfiguration {
    static config:OgmaConfiguration = new OgmaConfiguration();

    private _reorderAdjectives: boolean;
    private _autoApplyForDrop: boolean;
    private _discoveryContainerID: string;

    private constructor() {
        this._reorderAdjectives = true;
        this._autoApplyForDrop = true;
    }

    get reorderAdjectives(): boolean {
        return this._reorderAdjectives;
    }

    set reorderAdjectives(reorder: boolean) {
        this._reorderAdjectives = reorder;
    }

    get autoApplyForDrop(): boolean {
        return this._autoApplyForDrop;
    }

    set autoApplyForDrop(autoApply: boolean) {
        this._autoApplyForDrop = autoApply;
    }

    get discoveryContainerID(): string {
        return this._discoveryContainerID;
    }

    set discoveryContainerID(value: string) {
        this._discoveryContainerID = value;
    }
}

interface Tense {
    getTense(): any;
    setTense(tense: any);
}

interface PartOfSpeech {
    getType(): PartOfSpeechType;
    setType(type: PartOfSpeechType): void;

    getLexeme(grammaticalGender? : GrammaticalGender,
              grammaticalNumber?: GrammaticalNumber,
              tense?: Tense): string;
    setLexeme(lexeme: string,
              grammaticalGender?: GrammaticalGender,
              grammaticalNumber?: GrammaticalNumber,
              tense?: Tense): void;
}

interface PartOfSpeechInstance {
    getLexeme(): string;
    getPartOfSpeechType(): PartOfSpeechType;
    getPartOfSpeech(): PartOfSpeech;
    getGrammaticalNumber(): GrammaticalNumber;
}

interface WordSequence {

}

class GrammaticalContext {
    private _outerContext: GrammaticalContext;
    private _gender: GrammaticalGender;
    private _number: GrammaticalNumber;
    private _tense: EnglishLanguageTense;

    constructor(context: GrammaticalContext, grammaticalGender?: GrammaticalGender,
                grammaticalNumber?: GrammaticalNumber, englishLanguageTense?: EnglishLanguageTense) {
        this._outerContext = (context === undefined) ? null : context;
        this._gender = (grammaticalGender !== undefined) ? grammaticalGender : GrammaticalGender.UNKNOWN;
        this._number = (grammaticalNumber !== undefined) ? grammaticalNumber : GrammaticalNumber.NONE;
        this._tense = (englishLanguageTense !== undefined) ? englishLanguageTense : EnglishLanguageTense.UNKNOWN;
    }

    get outerContext(): GrammaticalContext {
        return this.outerContext;
    }

    set outerContext(context: GrammaticalContext) {
        this._outerContext = context;
    }

    get gender(): GrammaticalGender {
        if (this._gender !== GrammaticalGender.UNKNOWN)
            return this._gender;
        else if (this._outerContext !== null) {
            return this._outerContext.gender;
        } else {
            return this._gender;
        }
    }

    set gender(grammaticalGender: GrammaticalGender) {
        this._gender = grammaticalGender;
    }

    get number(): GrammaticalNumber {
        if (this._number !== GrammaticalNumber.NONE)
            return this._number;
        else if (this._outerContext !== null) {
            return this._outerContext.number;
        } else {
            return GrammaticalNumber.NONE;
        }
    }

    set number(grammaticalNumber: GrammaticalNumber) {
        this._number = grammaticalNumber;
    }

    get tense(): EnglishLanguageTense {
        if (this._tense !== EnglishLanguageTense.UNKNOWN)
            return this._tense;
        else if (this._outerContext !== null) {
            return this._outerContext.tense;
        } else {
            return this._tense;
        }
    }

    set tense(grammaticalTense: EnglishLanguageTense) {
        this._tense = grammaticalTense;
    }
}

abstract class HumanLanguagePartOfSpeech extends OgmaObject implements PartOfSpeech {
    protected _type: PartOfSpeechType;
    protected _lexeme:string;
    protected _baseCSSClass: string = undefined;
    protected _cssSelectedSuffix: string = undefined;
    protected _isSelected: boolean = false;

    get type(): PartOfSpeechType {
        return this._type;
    }

    set type(type:PartOfSpeechType) {
        this._type = type;
    }

    get lexeme(): string {
        return this._lexeme;
    }

    set lexeme(lexeme: string) {
        this._lexeme = lexeme;
    }

    getType(): PartOfSpeechType {
        return this._type;
    }

    setType(type: PartOfSpeechType): void {
        this._type = type;
    }

    getLexeme(grammaticalGender? : GrammaticalGender,
              grammaticalNumber?: GrammaticalNumber,
              tense?: Tense): string {
        return this._lexeme;
    }

    setLexeme(lexeme: string,
              grammaticalGender?: GrammaticalGender,
              grammaticalNumber?: GrammaticalNumber,
              tense?: Tense): void {
        this._lexeme = lexeme;
    }

    get gender(): GrammaticalGender {
        return GrammaticalGender.UNKNOWN;
    }

    set gender(g: GrammaticalGender) {
        this.restrictGender(g);
    }

    get grammaticalNumber(): GrammaticalNumber {
        return GrammaticalNumber.NONE;
    }

    set grammaticalNumber(n: GrammaticalNumber) {
        this.restrictNumber(n);
    }

    protected restrictGender(g: GrammaticalGender): GrammaticalGender {
        return g;
    }

    protected restrictNumber (n: GrammaticalNumber): GrammaticalNumber {
        return n;
    }

    get cssClass(): string {
        if (this._isSelected)
            return this._baseCSSClass + "-selected";
        else
            return this._baseCSSClass;
    }
}

abstract class EnglishPartOfSpeech extends HumanLanguagePartOfSpeech {
    protected _plurality: EnglishPlurality;

    protected restrictGender(g: GrammaticalGender): GrammaticalGender {
        switch (g) {
            case GrammaticalGender.UNKNOWN:
            case GrammaticalGender.NEUTER:
            case GrammaticalGender.FEMININE:
            case GrammaticalGender.MASCULINE:
                return g;
            default:
                alert("Attempt to set the grammatical gender of an EnglishPartOfSpeech to " + g.toString());
                return GrammaticalGender.UNKNOWN;
        }
    }

    protected restrictNumber (n: GrammaticalNumber): GrammaticalNumber {
        switch (n) {
            case GrammaticalNumber.NONE:
            case GrammaticalNumber.SINGULAR:
            case GrammaticalNumber.PLURAL:
                return n;
            default:
                alert("Attempt to set the grammatical number of an EnglishPartOfSpeech to " + n.toString());
                return GrammaticalNumber.NONE;
        }
    }

    getPlurality(): EnglishPlurality {
        return this._plurality;
    }
}

interface Noun {
    getLexeme(): string;
    getNounType(): NounType;
    getGender(): GrammaticalGender;
    getGrammaticalNumber(): GrammaticalNumber;
    getNounCountabilityType(): NounCountabilityType;
    isAlwaysPlural(): boolean;
    getPluralLexeme(): string;
    getAppropriateAdjectives(): Array<Adjective>;
}

class EnglishNoun
    extends EnglishPartOfSpeech
    // While noun implements OgmaDroppableContainer, it isn't really droppable, but it share in being something
    // that an item can be dragged from.
    implements Noun, Allocator<EnglishNounInstance>, IOgmaHTMLBoundObject, OgmaDroppableContainer {
    protected ogmaHTMLElements: ArrayList<OgmaHTMLElement>;
    protected _activeNounInstances: LinearSet<EnglishNounInstance>;
    protected _deallocatedNounInstances: LinearSet<EnglishNounInstance>;
    protected _floatingInstance: EnglishNounInstance;
    protected _nounType: NounType;
    protected _gender: GrammaticalGender;
    protected _grammaticalNumber: GrammaticalNumber;
    protected _countability: NounCountabilityType;
    protected _alwaysPlural: boolean;
    protected _pluralLexeme: string;
    protected _appropriateAdjectives: LinearSet<EnglishAdjective>;
    protected _myDiv: OgmaHTMLDivElement;
    protected _lexemeSpan: OgmaHTMLSpanElement;
    protected _nounInstDiv: OgmaHTMLDivElement;

    constructor (singularLexeme: string, nounType: NounType, gender: GrammaticalGender,
                 countability: NounCountabilityType, pluralLexeme?: string, alwaysPlural?: boolean,
                 englishPlurality?: EnglishPlurality) {
        super();
        this.ogmaHTMLElements = new ArrayList<OgmaHTMLElement>();
        this._activeNounInstances = new LinearSet<EnglishNounInstance>();
        this._deallocatedNounInstances = new LinearSet<EnglishNounInstance>();
        this._floatingInstance = null;
        this._nounType = nounType;
        this._type = PartOfSpeechType.NOUN;
        this._lexeme = singularLexeme;
        this.gender = gender;
        this._grammaticalNumber = GrammaticalNumber.SINGULAR;
        this.countability = (countability === undefined) ? NounCountabilityType.COUNTABLE : countability;
        this._alwaysPlural = (alwaysPlural === undefined) ? false : alwaysPlural;
        this._plurality = (englishPlurality === undefined)? EnglishPlurality.UNKNOWN_PLURALITY : englishPlurality;
        this._pluralLexeme = (pluralLexeme === undefined)
            ? ((this._alwaysPlural) ? this._lexeme : null)
            : pluralLexeme;
        this._appropriateAdjectives = new LinearSet<EnglishAdjective>();

        this.initHTML();
    }

    protected initHTML() {
        this._myDiv = this.addOgmaHTMLElement(new OgmaHTMLDivElement(this)) as OgmaHTMLDivElement;
        this._myDiv.setAttributeValue("data-ogma", "EnglishNoun/myDiv");
        this._nounInstDiv = null;

        this._lexemeSpan = this.addOgmaHTMLElement(new OgmaHTMLSpanElement(this)) as OgmaHTMLSpanElement;
        this._lexemeSpan.setAttributeValue("data-ogma", "EnglishNoun/myDiv/lexemeSpan");
        this._lexemeSpan.className = this.cssClass;
        this._lexemeSpan.registerInterestInEventType("click");
        this._lexemeSpan.registerInterestInEventType("mouseenter");
        this._lexemeSpan.registerInterestInEventType("mouseleave");

        this._myDiv.registerInterestInEventType("click");
        this._myDiv.registerInterestInEventType("mouseenter");
        this._myDiv.registerInterestInEventType("mouseleave");

        this._myDiv.appendChild(this._lexemeSpan);

        this._lexemeSpan.text = this.getLexeme();
    }

    getOgmaHTML(): OgmaHTMLElement {
        return this._myDiv;
    }

    getHTML(): HTMLDivElement {
        return this._myDiv.htmlElement as HTMLDivElement;
    }

    alloc(): EnglishNounInstance {
        let instance: EnglishNounInstance = this._deallocatedNounInstances.removeRandomElement();

        if (instance == null) {
            instance = new EnglishNounInstance(this);
        }

        this._activeNounInstances.add(instance);

        return instance;
    }

    dealloc(something: EnglishNounInstance) {
        if ((something === undefined) || (something === null)) return;

        if (! this._activeNounInstances.contains(something)) {
            return;
        }

        this._activeNounInstances.remove(something);
        this._deallocatedNounInstances.add(something);
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
        switch (eventType) {
            case "mouseenter":
                if (this._floatingInstance !== null) {
                    this.dealloc(this._floatingInstance);
                }

                this._floatingInstance = this.alloc();

                if (this._nounInstDiv !== null) {
                    this._myDiv.removeChild(this._nounInstDiv);
                }

                this._nounInstDiv = this._floatingInstance.getOgmaHTML();

                this._myDiv.removeChild(this._lexemeSpan);
                this._myDiv.appendChild(this._nounInstDiv);

                break;
            case "mouseleave":
                if (this._nounInstDiv !== null) {
                    this._myDiv.removeChild(this._nounInstDiv);
                    this._nounInstDiv = null;
                }

                if (this._floatingInstance !== null) {
                    this.dealloc(this._floatingInstance);
                    this._floatingInstance = null;
                }

                this._myDiv.appendChild(this._lexemeSpan);

                break;
        }
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

    getLexeme(): string {
        return this._lexeme;
    }

    getNounType(): NounType {
        return this._nounType;
    }

    get nounType(): NounType {
        return this._nounType;
    }

    set nounType(nt: NounType) {
        this._nounType = nt;
    }

    getGender(): GrammaticalGender {
        return this._gender;
    }

    get gender(): GrammaticalGender {
        return this._gender;
    }

    set gender(g: GrammaticalGender) {
        let filteredGender: GrammaticalGender = this.restrictGender(g);
        this._gender = filteredGender;
    }

    getGrammaticalNumber(): GrammaticalNumber {
        return this._grammaticalNumber;
    }

    get grammaticalNumber(): GrammaticalNumber {
        return this._grammaticalNumber;
    }

    set grammaticalNumber(n: GrammaticalNumber) {
        this._grammaticalNumber = this.restrictNumber(n);
    }

    getNounCountabilityType(): NounCountabilityType {
        return this._countability;
    }

    get countability(): NounCountabilityType {
        return this._countability;
    }

    set countability(c: NounCountabilityType) {
        this._countability = c;
    }

    isAlwaysPlural(): boolean {
        return this._alwaysPlural;
    }

    get alwaysPlural(): boolean {
        return this._alwaysPlural;
    }

    set alwaysPlural(v: boolean) {
        this._alwaysPlural = v;
    }

    getPlurality(): EnglishPlurality {
        return this._plurality;
    }

    get plurality(): EnglishPlurality {
        return this._plurality;
    }

    set plurality(p: EnglishPlurality) {
        this._plurality = p;
    }

    getPluralLexeme(): string {
        return this._pluralLexeme;
    }

    get pluralLexeme(): string {
        return this._pluralLexeme;
    }

    set pluralLexeme(lex: string) {
        this._pluralLexeme = lex;
    }

    addAppropriateAdjective(adj: EnglishAdjective): void {
        this._appropriateAdjectives.add(adj);
    }

    getAppropriateAdjectives(): Array<Adjective> {
        // @ts-ignore
        return this._appropriateAdjectives.toArray() as Array<Adjective>;
    }

    get cssClass(): string {
        if (this._baseCSSClass == undefined) {
            this._baseCSSClass = "noun";
            this._cssSelectedSuffix = "-selected";
        }

        if (this._isSelected) {
            return this._baseCSSClass + this._cssSelectedSuffix;
        } else {
            return this._baseCSSClass;
        }
    }

    applyForAcceptance(draggable: OgmaDraggableObject): boolean {
        // Nouns never accept a drop.
        return false;
    }

    acceptsDrop(draggable: OgmaDraggableObject): boolean {
        // Nouns never accept a drop.
        return false;
    }

    protected onDropSourceAndDestination(source: OgmaObject, destination: OgmaObject): boolean {
        // Nouns never accept a drop.
        return false;
    }

    onDrop(event: OgmaHTMLDragEvent): boolean {
        // Nouns never accept a drop.
        return false;
    }

    onDraggedFrom(draggable: OgmaDraggableObject): void {
        console.log("EnglishNoun.onDraggedFrom(): draggable=" + draggable);
        this._floatingInstance = this.alloc();
        this._nounInstDiv = this._floatingInstance.getOgmaHTML();
        this._myDiv.appendChild(this._nounInstDiv);
    }

    toWordSequence(): WordSequence {
        return null;
    }
}

class EnglishNounInstance
    extends OgmaDoubleLinkedList<OgmaDoubleLinkedListElement>
    implements PartOfSpeechInstance, IOgmaHTMLBoundObject, OgmaDraggableObject {
    protected ogmaHTMLElements: ArrayList<OgmaHTMLElement>;
    protected _parentDroppableContainer: OgmaDroppableContainer;
    protected _droppableContainers: LinearSet<OgmaDroppableContainer>;
    protected _myDiv: OgmaHTMLDivElement;
    protected _lexemeSpan: OgmaHTMLSpanElement;
    protected _modifiersDiv: OgmaHTMLDivElement;
    private _noun: EnglishNoun;
    protected _baseCSSClass: string = "nouninstance";
    protected _cssSelectedSuffix: string = "-selected";
    protected _isSelected: boolean = false;
    protected _grammaticalNumber: GrammaticalNumber = GrammaticalNumber.SINGULAR;
    protected _phrasalParent: any;

    constructor(noun: EnglishNoun) {
        super();
        this.ogmaHTMLElements = new ArrayList<OgmaHTMLElement>();
        this._noun = noun;
        this._parentDroppableContainer = noun;
        this._phrasalParent = null;

        this.initHTML();
        this.initEvents();
    }

    protected initHTML() {
        this._myDiv = this.addOgmaHTMLElement(new OgmaHTMLDivElement(this)) as OgmaHTMLDivElement;
        this._myDiv.setAttributeValue("data-ogma", "EnglishNounInstance/myDiv");
        this._myDiv.setAttributeValue("draggable", "true");
        this._myDiv.display = "inline";

        this._modifiersDiv = null;

        this._lexemeSpan = this.addOgmaHTMLElement(new OgmaHTMLSpanElement(this)) as OgmaHTMLSpanElement;
        this._lexemeSpan.setAttributeValue("data-ogma", "EnglishNounInstance/myDiv/lexemeSpan");
        this._lexemeSpan.className = this.cssClass;
        this._lexemeSpan.registerInterestInEventType("click");
        this._lexemeSpan.registerInterestInEventType("mouseenter");
        this._lexemeSpan.registerInterestInEventType("mouseleave");

        this._myDiv.registerInterestInEventType("click");
        this._myDiv.registerInterestInEventType("mouseenter");
        this._myDiv.registerInterestInEventType("mouseleave");

        this._myDiv.appendChild(this._lexemeSpan);

        this._lexemeSpan.text = this.getLexeme();
    }

    protected initEvents():void {
        let self: EnglishNounInstance = this;

        this._myDiv.htmlElement.ondragstart = function (e: Event) {
            console.log("EnglishNounInstance.ondragstart(): e=" + e);
            let dragEvent: OgmaHTMLDragEvent = new OgmaHTMLDragEvent(e);

            console.log("  dragEvent=" + dragEvent + ", dragEvent.dataTransfer=" + dragEvent.dataTransfer);

            dragEvent.dataTransfer.ogmaSource = self;
        }

        this._myDiv.htmlElement.ondrag = function (e:Event) {
            self.onDrag();
        }
    }

    getOgmaHTML(): OgmaHTMLElement {
        return this._myDiv;
    }

    getHTML(): HTMLDivElement {
        return this._myDiv.htmlElement as HTMLDivElement;
    }

    getLexeme(): string {
        switch (this.grammaticalNumber) {
            case GrammaticalNumber.SINGULAR:
                return this._noun.getLexeme();
            case GrammaticalNumber.PLURAL:
            case GrammaticalNumber.DUAL:
            case GrammaticalNumber.TRIAL:
            case GrammaticalNumber.QUADRAL:
                return this._noun.getPluralLexeme();
            case GrammaticalNumber.COLLECTIVE:
                return this._noun.getLexeme();
            case GrammaticalNumber.NONE:
                return this._noun.getLexeme();
            default:
                this._noun.getLexeme();
        }
    }

    getPartOfSpeechType(): PartOfSpeechType {
        return this._noun.getType();
    }

    getPartOfSpeech(): PartOfSpeech {
        return this._noun;
    }

    get noun(): EnglishNoun {
        return this._noun;
    }

    get gender(): GrammaticalGender {
        return this._noun.gender;
    }

    get phrasalParent(): any {
        return this._phrasalParent
    }

    set phrasalParent(value: any) {
        this._phrasalParent = value;
    }

    getGrammaticalNumber(): GrammaticalNumber {
        return this.grammaticalNumber;
    }

    get grammaticalNumber(): GrammaticalNumber {
        if (this._noun.isAlwaysPlural())
            return GrammaticalNumber.PLURAL;
        else
            return this._grammaticalNumber;
    }

    set grammaticalNumber(number: GrammaticalNumber) {
        this._grammaticalNumber = number;
    }

    get nounType(): NounType {
        return this._noun.nounType;
    }

    get countability(): NounCountabilityType {
        return this._noun.countability;
    }

    getPlurality(): EnglishPlurality {
        return this._noun.getPlurality();
    }

    get alwaysPlural(): boolean {
        return this._noun.alwaysPlural;
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
        console.log("  received " + eventType + " event from " + source.toString());
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

    get cssClass(): string {
        if (this._isSelected) {
            return this._baseCSSClass + this._cssSelectedSuffix;
        } else {
            return this._baseCSSClass;
        }
    }

    addDroppableContainer(droppable: OgmaDroppableContainer): void {
        if (droppable.applyForAcceptance(this)) {
            this._droppableContainers.add(droppable)
        }
    }

    addDroppableContainers(droppables: Array<OgmaDroppableContainer>): void {
        droppables.forEach(function (droppable) {
            this.addDroppableContainer(droppable);
        })
    }

    removeDroppableContainer(droppable: OgmaDroppableContainer): void {
        if (this._droppableContainers.contains(droppable))
            this._droppableContainers.remove(droppable);
    }

    removeAllDroppableContainers(): void {
        this._droppableContainers.clear();
    }

    getParentDroppableContainer(): OgmaDroppableContainer {
        return this._parentDroppableContainer;
    }

    onDrag(): void {

    }

    onDropped(droppable: OgmaDroppableContainer): void {
        console.log("EnglishNounInstance.onDropped(): droppable=" + droppable);
        if (OgmaConfiguration.config.autoApplyForDrop || (this._droppableContainers.contains(droppable))) {
            this._parentDroppableContainer.onDraggedFrom(this);
            this._parentDroppableContainer = droppable;
        }
    }

    toPartOfSpeechInstance(): PartOfSpeechInstance {
        return this;
    }

    toString(): string {
        return "|" + this.getLexeme() + "|";
    }
}

interface Adjective {
    getLexeme(): string;
    getCategory(): AdjectiveCategory;
    getAdjectiveType(): AdjectiveType;
    getPriority(): AdjectivalOrderPriority;
    getRankInPriority(): number;
    getConflicts(): Array<Adjective>;
    getNouns(): Array<Noun>;
}

class EnglishAdjective
    extends EnglishPartOfSpeech
    // While adjective implements OgmaDroppableContainer, it isn't really droppable, but it share in being something
    // that an item can be dragged from.
    implements Adjective, Allocator<EnglishAdjectiveInstance>, IOgmaHTMLBoundObject, OgmaDroppableContainer {
    protected ogmaHTMLElements: ArrayList<OgmaHTMLElement>;
    protected _activeAdjectiveInstances: LinearSet<EnglishAdjectiveInstance>;
    protected _deallocatedAdjectiveInstances: LinearSet<EnglishAdjectiveInstance>;
    protected _category: EnglishAdjectiveCategory;
    protected _adjectiveType: AdjectiveType;
    protected _priority: AdjectivalOrderPriority;
    protected _rankInPriority: number;
    protected _conflicts: Array<EnglishAdjective>;
    protected _nouns: Array<EnglishNoun>;
    protected _instances: LinearSet<EnglishAdjectiveInstance>;
    protected _floatingInstance: EnglishAdjectiveInstance;
    protected _myDiv: OgmaHTMLDivElement;
    protected _lexemeSpan: OgmaHTMLSpanElement;
    protected _adjInstDiv: OgmaHTMLDivElement;

    constructor (lex: string, subtype?: AdjectiveType, priority?: AdjectivalOrderPriority, rankInPriority?: number, englishPlurality?: EnglishPlurality) {
        super();
        this.ogmaHTMLElements = new ArrayList<OgmaHTMLElement>();
        this._activeAdjectiveInstances = new LinearSet<EnglishAdjectiveInstance>();
        this._deallocatedAdjectiveInstances = new LinearSet<EnglishAdjectiveInstance>();
        this.lexeme = lex;
        this.type = PartOfSpeechType.ADJECTIVE;
        this.adjectiveType = (subtype === undefined) ? AdjectiveType.ADJECTIVE : subtype;
        this._plurality = (englishPlurality === undefined) ? EnglishPlurality.UNKNOWN_PLURALITY : englishPlurality;
        this.priority = (priority === undefined) ? AdjectivalOrderPriority.OPINION : priority;
        this.rankInPriority = (rankInPriority === undefined)? 0 : rankInPriority;
        this._floatingInstance = null;

        this._conflicts = new Array<EnglishAdjective>();
        this._nouns = new Array<EnglishNoun>();
        this._instances = new LinearSet<EnglishAdjectiveInstance>();

        this.initHTML();
    }

    protected initHTML() {
        this._myDiv = this.addOgmaHTMLElement(new OgmaHTMLDivElement(this)) as OgmaHTMLDivElement;
        this._myDiv.setAttributeValue("data-ogma", "EnglishAdjective/myDiv");
        this._adjInstDiv = null;

        this._lexemeSpan = this.addOgmaHTMLElement(new OgmaHTMLSpanElement(this)) as OgmaHTMLSpanElement;
        this._lexemeSpan.setAttributeValue("data-ogma", "EnglishAdjective/myDiv/lexemeSpan");
        this._lexemeSpan.className = this.cssClass;
        this._lexemeSpan.registerInterestInEventType("click");
        this._lexemeSpan.registerInterestInEventType("mouseenter");
        this._lexemeSpan.registerInterestInEventType("mouseleave");

        this._myDiv.registerInterestInEventType("click");
        this._myDiv.registerInterestInEventType("mouseenter");
        this._myDiv.registerInterestInEventType("mouseleave");

        this._myDiv.appendChild(this._lexemeSpan);

        this._lexemeSpan.text = this.getLexeme();
    }

    getOgmaHTML(): OgmaHTMLElement {
        return this._myDiv;
    }

    getHTML(): HTMLDivElement {
        return this._myDiv.htmlElement as HTMLDivElement;
    }

    alloc(): EnglishAdjectiveInstance {
        let instance: EnglishAdjectiveInstance = this._deallocatedAdjectiveInstances.removeRandomElement();

        if (instance == null) {
            console.log("alloc(): adjectiveType=" + this._adjectiveType);
            switch (this._adjectiveType) {
                case AdjectiveType.ARTICLE:
                    instance = new EnglishArticleInstance(this);
                    break;
                case AdjectiveType.DEMONSTRATIVE:
                    instance = new EnglishDemonstrativeInstance(this);
                    break;
                case AdjectiveType.DETERMINER:
                    instance = new EnglishCentralDeterminerInstance(this);
                    break;
                case AdjectiveType.POSSESSIVE:
                    instance = new EnglishPossessiveInstance(this);
                    break;
                case AdjectiveType.QUANTIFIER:
                    instance = new EnglishQuantifierInstance(this);
                    break;
                default:
                    instance = new EnglishAdjectiveInstance(this);
                    break;
            }
        }

        this._activeAdjectiveInstances.add(instance);

        return instance;
    }

    dealloc(something: EnglishAdjectiveInstance) {
        if ((something === undefined) || (something === null)) return;

        if (! this._activeAdjectiveInstances.contains(something)) {
            return;
        }

        this._activeAdjectiveInstances.remove(something);
        this._deallocatedAdjectiveInstances.add(something);
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

        console.log("  received " + eventType + " event from " + source.toString());

        switch (eventType) {
            case "mouseenter":
                if (this._floatingInstance !== null) {
                    this.dealloc(this._floatingInstance);
                }

                this._floatingInstance = this.alloc();

                if (this._adjInstDiv !== null) {
                    this._myDiv.removeChild(this._adjInstDiv);
                }

                this._adjInstDiv = this._floatingInstance.getOgmaHTML();

                this._myDiv.removeChild(this._lexemeSpan);
                this._myDiv.appendChild(this._adjInstDiv);

                break;
            case "mouseleave":
                if (this._adjInstDiv !== null) {
                    this._myDiv.removeChild(this._adjInstDiv);
                    this._adjInstDiv = null;
                }

                if (this._floatingInstance !== null) {
                    this.dealloc(this._floatingInstance);
                    this._floatingInstance = null;
                }

                this._myDiv.appendChild(this._lexemeSpan);

                break;
        }
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

    getLexeme(): string {
        return this._lexeme;
    }

    getCategory(): AdjectiveCategory {
        return this._category;
    }

    get category(): EnglishAdjectiveCategory {
        return this._category;
    }

    set category(category: EnglishAdjectiveCategory) {
        this._category = category;
    }

    getAdjectiveType(): AdjectiveType {
        return this._adjectiveType;
    }

    get adjectiveType(): AdjectiveType {
        return this._adjectiveType;
    }

    set adjectiveType(at: AdjectiveType) {
        this._adjectiveType = at;
    }

    getPlurality(): EnglishPlurality {
        return this._plurality;
    }

    get plurality(): EnglishPlurality {
        return this._plurality;
    }

    set plurality(p: EnglishPlurality) {
        this._plurality = p;
    }

    getPriority(): AdjectivalOrderPriority {
        return this._priority;
    }

    get priority(): AdjectivalOrderPriority {
        return this._priority;
    }

    set priority(p: AdjectivalOrderPriority) {
        this._priority = p;
    }

    getRankInPriority(): number {
        return this._rankInPriority;
    }

    get rankInPriority(): number {
        return this._rankInPriority;
    }

    set rankInPriority(n: number) {
        this._rankInPriority = n;
    }

    addConflict(adj: EnglishAdjective): void {
        this._conflicts.push(adj);
    }

    conflicts(adj: EnglishAdjective): boolean {
        console.log("EnglishAdjective.conflicts(): this=" + this.toString() + ", adj=" + adj.toString());
        return this._conflicts.indexOf(adj) != -1;
    }

    getConflicts(): Array<Adjective> {
        // @ts-ignore
        return this._conflicts as Array<Adjective>;
    }

    appropriateForNoun(n: EnglishNoun): boolean {
        console.log("EnglishAdjective.appropriateForNoun(): n=" + n.toString + ", this._nouns=" + this._nouns.toString());
        return this._nouns.indexOf(n) != -1;
    }

    addNoun(n: EnglishNoun): void {
        this._nouns.push(n);
    }

    getNouns(): Array<Noun> {
        return this._nouns as Array<Noun>;
    }

    compareTo(other: OgmaObject): number {
        if (other instanceof EnglishAdjective) {
            let myIndex: number = this.priority + (this.rankInPriority / 10000);
            let otherIndex: number = (other as EnglishAdjective).priority + ((other as EnglishAdjective).rankInPriority / 10000);

            if (myIndex == otherIndex)
                return 0;
            else if (myIndex < otherIndex) {
                return -1;
            } else
                return 1;
        } else
            return super.compareTo(other);
    }

    samePriority(other: EnglishAdjective): boolean {
        return this.priority == other.priority;
    }

    get cssClass(): string {
        if (this._baseCSSClass == undefined) {
            this._baseCSSClass = "adjective";
            this._cssSelectedSuffix = "-selected";
        }

        if (this._isSelected) {
            return this._baseCSSClass + this._cssSelectedSuffix;
        } else {
            return this._baseCSSClass;
        }
    }

    applyForAcceptance(draggable: OgmaDraggableObject): boolean {
        // Adjectives never accept a drop.
        return false;
    }

    acceptsDrop(draggable: OgmaDraggableObject): boolean {
        // Adjectives never accept a drop.
        return false;
    }

    protected onDropSourceAndDestination(source: OgmaObject, destination: OgmaObject): boolean {
        // Adjectives never accept a drop.
        return false;
    }

    onDrop(event: OgmaHTMLDragEvent): boolean {
        // Adjectives never accept a drop.
        return false;
    }

    onDraggedFrom(draggable: OgmaDraggableObject): void {
        console.log("EnglishAdjective.onDraggedFrom(): draggable=" + draggable);
        this._floatingInstance = this.alloc();
        this._adjInstDiv = this._floatingInstance.getOgmaHTML();
        this._myDiv.appendChild(this._adjInstDiv);
    }

    toWordSequence(): WordSequence {
        return null;
    }
}

interface AdjectiveCategory {
    getName(): string;
    getPriority(): AdjectivalOrderPriority;
    getAdjectives(): Array<Adjective>;
    isMutuallyExclusive(): boolean;
}

class EnglishAdjectiveCategory extends OgmaObject implements AdjectiveCategory, IOgmaHTMLBoundObject {
    protected ogmaHTMLElements: ArrayList<OgmaHTMLElement>;
    protected _name: string;
    protected _priority: AdjectivalOrderPriority;
    protected _adjectives: LinearSet<EnglishAdjective>;
    protected _mutuallyExclusive: boolean;

    constructor(priority: AdjectivalOrderPriority, name: string, mutuallyExclusive?: boolean) {
        super();
        this.ogmaHTMLElements = new ArrayList<OgmaHTMLElement>();
        this._name = name;
        this._priority = priority;
        this._adjectives = new LinearSet<EnglishAdjective>();
        this._mutuallyExclusive = (mutuallyExclusive == undefined) ? false : mutuallyExclusive;
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
    getName(): string {
        return this._name;
    }

    getPriority(): AdjectivalOrderPriority {
        return this._priority;
    }

    getAdjectives(): Array<Adjective> {
        // @ts-ignore
        return this._adjectives.toArray() as Array<Adjective>;
    }

    isMutuallyExclusive(): boolean {
        return this._mutuallyExclusive;
    }

    get name(): string {
        return this._name;
    }

    get priority(): AdjectivalOrderPriority {
        return this._priority;
    }

    set priority(p: AdjectivalOrderPriority) {
        this._priority = p;
    }

    addAdjective(adj: EnglishAdjective):void {
        this._adjectives.add(adj);
    }

    compareTo(other: OgmaObject): number {
        if (other instanceof EnglishAdjectiveCategory) {
            if (this._priority == (other as EnglishAdjectiveCategory).priority)
                return 0;
            else if (this._priority < (other as EnglishAdjectiveCategory).priority)
                return -1;
            else
                return 1;
        }
    }
}

// The classes below will be moved to a viewer class file once the model is complete.

enum EnglishAdjectiveDisharmoniousState {
    IN_CONFLICT,
    INAPPROPRIATE,
    DISORDERED,
    MORE_THAN_ONE_CENTRAL_DETERMINER,
    AN_BEFORE_CONSONANT,
    A_BEFORE_VOWEL,
    ARTICLE_MODIFYING_PROPER_NOUN,
    INDEFINITE_ARTICLE_MODIFYING_UNCOUNTABLE_NOUN,
    MISMATCH_IN_NUMBER
}

class EnglishAdjectiveDisharmonies {
    _subject: PartOfSpeechInstance;
    _conflicts: LinearSet<PartOfSpeechInstance>;
    _inappropriate: LinearSet<PartOfSpeechInstance>;
    _moreThanOneCentralDeterminer: LinearSet<PartOfSpeechInstance>;
    _disorderlyLeftNeighbor: PartOfSpeechInstance;
    _disorderlyRightNeighbor: PartOfSpeechInstance;
    _anBeforeConstant: PartOfSpeechInstance;
    _aBeforeVowel: PartOfSpeechInstance;
    _articleModifyingProperNoun: PartOfSpeechInstance;
    _indefiniteArticleModifyingUncountableNoun: PartOfSpeechInstance;
    _mismatchInNumber: PartOfSpeechInstance;

    constructor(subject: PartOfSpeechInstance) {
        this._subject = subject;
        this._conflicts = new LinearSet<PartOfSpeechInstance>();
        this._inappropriate = new LinearSet<PartOfSpeechInstance>();
        this._moreThanOneCentralDeterminer = new LinearSet<PartOfSpeechInstance>();
        this._disorderlyLeftNeighbor = null;
        this._disorderlyRightNeighbor = null;
        this._anBeforeConstant = null;
        this._aBeforeVowel = null;
        this._articleModifyingProperNoun = null;
        this._indefiniteArticleModifyingUncountableNoun = null;
        this._mismatchInNumber = null;
    }

    clear(): void {
        this._conflicts.clear();
        this._inappropriate.clear();
        this._moreThanOneCentralDeterminer.clear();
        this._disorderlyLeftNeighbor = null;
        this._disorderlyRightNeighbor = null;
        this._anBeforeConstant = null;
        this._aBeforeVowel = null;
        this._articleModifyingProperNoun = null;
        this._indefiniteArticleModifyingUncountableNoun = null;
    }

    isClear(): boolean {
        return (this._conflicts.size() == 0) && (this._inappropriate.size() == 0)
                 && (this._disorderlyLeftNeighbor == null) && (this._disorderlyRightNeighbor == null)
                 && (this._moreThanOneCentralDeterminer.size() == 0)
                 && (this._anBeforeConstant == null) && (this._aBeforeVowel == null)
                 && (this._articleModifyingProperNoun == null) && (this._indefiniteArticleModifyingUncountableNoun == null)
                 && (this._mismatchInNumber == null);
    }

    add(instance:PartOfSpeechInstance, reason: EnglishAdjectiveDisharmoniousState, left?: boolean) :void {
        switch (reason) {
            case EnglishAdjectiveDisharmoniousState.IN_CONFLICT:
                console.log("*** in conflict ***");
                this._conflicts.add(instance);
                break;
            case EnglishAdjectiveDisharmoniousState.DISORDERED:
                if (left !== undefined) {
                    if (left) {
                        this._disorderlyLeftNeighbor = instance;
                    } else {
                        this._disorderlyRightNeighbor = instance;
                    }
                }
                break;
            case EnglishAdjectiveDisharmoniousState.INAPPROPRIATE:
                this._inappropriate.add(instance);
                break;
            case EnglishAdjectiveDisharmoniousState.MORE_THAN_ONE_CENTRAL_DETERMINER:
                this._moreThanOneCentralDeterminer.add(instance);
                break;
            case EnglishAdjectiveDisharmoniousState.AN_BEFORE_CONSONANT:
                this._anBeforeConstant = instance;
                break;
            case EnglishAdjectiveDisharmoniousState.A_BEFORE_VOWEL:
                this._aBeforeVowel = instance;
                break;
            case EnglishAdjectiveDisharmoniousState.ARTICLE_MODIFYING_PROPER_NOUN:
                this._articleModifyingProperNoun = instance;
                break;
            case EnglishAdjectiveDisharmoniousState.INDEFINITE_ARTICLE_MODIFYING_UNCOUNTABLE_NOUN:
                this._indefiniteArticleModifyingUncountableNoun = instance;
                break;
            case EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER:
                this._mismatchInNumber = instance;
                break;
        }
    }

    toString(): string {
        let result: string = this._subject.toString() + " has the following disharmonies within its current context:";
        let valuesAsArray: Array<PartOfSpeechInstance>;
        let size: number;
        let i: number;

        size = this._moreThanOneCentralDeterminer.size();

        if (size > 0) {
            valuesAsArray = this._moreThanOneCentralDeterminer.toArray();

            for (i = 0; i < size; i++) {
                result += "\n    Only one central determiner is allowed per adjectival phrase. " + valuesAsArray[i].toString() + " conflicts with " + this._subject.toString() + ".";
            }
        }

        if ((this._disorderlyLeftNeighbor !== null) || (this._disorderlyRightNeighbor !== null)) {
            result += "\n  Ordering:";

            if (this._disorderlyLeftNeighbor !== null)
                result += "\n    preceding " + this._disorderlyLeftNeighbor.toString() + " cannot come before " + this._subject.toString();

            if (this._disorderlyRightNeighbor !== null)
                result += "\n    following " + this._disorderlyRightNeighbor.toString() + " cannot come after " + this._subject.toString();
        }

        size = this._conflicts.size();

        if (size > 0) {
            valuesAsArray = this._conflicts.toArray();

            result += "\n  Semantic Conflicts:"

            for (i = 0; i < size; i++) {
                result += "\n    The meaning of " + valuesAsArray[i].toString() + " conflicts with the meaning of " + this._subject.toString();
            }
        }

        size = this._inappropriate.size();

        if (size > 0) {
            valuesAsArray = this._inappropriate.toArray();

            result += "\n  Inappropriate Modifiers:"

            for (i = 0; i < size; i++) {
                result += "\n    " + this._subject.toString() + " is an unusual modifier for " + valuesAsArray[i].toString();
            }
        }

        return result;
    }

    get info(): string {
        let myInfo: string = "<ul>";
        let valuesAsArray: Array<PartOfSpeechInstance>;
        let size: number;
        let i: number;

        size = this._moreThanOneCentralDeterminer.size();

        if (size > 0) {
            valuesAsArray = this._moreThanOneCentralDeterminer.toArray();

            for (i = 0; i < size; i++) {
                myInfo += "<li>Only one central determiner is allowed per adjectival phrase. <b>"
                    + valuesAsArray[i].getLexeme() + "</b> conflicts with <b>" + this._subject.getLexeme() + "</b>.</li>";
            }
        }

        if (this._anBeforeConstant !== null) {
            myInfo += "<li>";
            myInfo += "The indefinite article <b>an</b> must appear only before a noun or adjective beginning with a vowel. ";
            myInfo += "In this case, it is followed by <b>" + this._anBeforeConstant.getLexeme() + "</b>, which begins with a consonant.";
            myInfo += "</li>";
        }

        if (this._aBeforeVowel !== null) {
            myInfo += "<li>";
            myInfo += "The indefinite article <b>a</b> must appear only before a noun or adjective beginning with a consonant. ";
            myInfo += "In this case, it is followed by <b>" + this._aBeforeVowel.getLexeme() + "</b>, which begins with a vowel.";
            myInfo += "</li>";
        }

        if ((this._disorderlyLeftNeighbor !== null) || (this._disorderlyRightNeighbor !== null)) {
            if (this._disorderlyLeftNeighbor !== null) {
                myInfo += ('<li>"' + this._disorderlyLeftNeighbor.getLexeme()
                    + '" has a higher precedence than "' + this._subject.getLexeme()
                    + '". It should come after "' + this._subject.getLexeme() + '".</li>');
            }

            if (this._disorderlyRightNeighbor !== null) {
                myInfo += ('<li>"' + this._disorderlyRightNeighbor.getLexeme()
                    + '" has a lower precedence than "' + this._subject.getLexeme()
                    + '". It should come before "' + this._subject.getLexeme() + '".</li>');
            }
        }

        size = this._conflicts.size();

        if (size > 0) {
            valuesAsArray = this._conflicts.toArray();

            for (i = 0; i < size; i++) {
                myInfo += "<li>The meaning of <b>" + valuesAsArray[i].getLexeme() + "</b> conflicts with the meaning of <b>" + this._subject.getLexeme() + "</b>.</li>";
            }
        }

        size = this._inappropriate.size();

        if (size > 0) {
            valuesAsArray = this._inappropriate.toArray();

            for (i = 0; i < size; i++) {
                myInfo += '<li>"' + this._subject.getLexeme() + '" is an unusual modifier for "' + valuesAsArray[i].getLexeme()
                    + '" Its use is not ungrammatical, but it is unusual for this adjective to modify this noun.</li>';
            }
        }

        if (this._articleModifyingProperNoun !== null) {
            myInfo += "<li>";
            myInfo += "<b>" + this._articleModifyingProperNoun.getLexeme() + "</b> is a proper noun. ";
            myInfo += "Except where more than one person, place, or thing could be identified by a proper noun, an article cannot modify the noun. ";
            myInfo += "In this case, the article <b>" + this._subject.getLexeme() + "</b> modifies the proper noun <b>" + this._articleModifyingProperNoun.getLexeme() + "</b>, ";
            myInfo += "which is usually incorrect.";
            myInfo += "</li>"
        }

        if (this._indefiniteArticleModifyingUncountableNoun !== null) {
            myInfo += "<li>";
            myInfo += "The noun <b>" + this._indefiniteArticleModifyingUncountableNoun.getLexeme() + "</b> is uncountable. "
            myInfo += "The indefinite article <b>" + this._subject.getLexeme() + "</b> modifies <b>" + this._indefiniteArticleModifyingUncountableNoun.getLexeme() + "</b>. ";
            myInfo += "Uncountable nouns cannot be modified by an indefinite article.";
            myInfo += "</li>"
        }

        if (this._mismatchInNumber !== null) {
            myInfo += "<li>";
            myInfo += "The noun <b>" + this._mismatchInNumber.getLexeme() + "</b> is ";

            switch (this._mismatchInNumber.getGrammaticalNumber()) {
                case GrammaticalNumber.SINGULAR:
                    myInfo += "singular";
                    break;
                case GrammaticalNumber.PLURAL:
                    myInfo += "plural";
                    break;
                default:
                    myInfo += "of unknown number";
                    break;
            }

            myInfo += " but the " + (this._subject as EnglishAdjectiveInstance).adjective.getCategory().getName().toLowerCase();
            myInfo += " <b>" + this._subject.getLexeme() + "</b> is " + EnglishPlurality[(this._subject as EnglishAdjectiveInstance).getPlurality()] + ".";
            myInfo += "</li>"; //!!!!!
        }

        return myInfo + "</ul>";
    }
}

class EnglishAdjectiveInstanceComparator implements Comparator<EnglishAdjectiveInstance> {
    static comparator: EnglishAdjectiveInstanceComparator = new EnglishAdjectiveInstanceComparator();

    compareTo(o1: EnglishAdjectiveInstance, o2: EnglishAdjectiveInstance): number {
        if (o1.priority < o2.priority)
            return -1;
        else if (o1.priority > o2.priority)
            return 1;
        else if (o1.rankInPriority < o2.rankInPriority)
            return -1;
        else if (o1.rankInPriority > o2.rankInPriority)
            return 1;
        else
            return 0;
    }
}

const ADJECTIVE_NULL_DELIMITER:string = "";
const ADJECTIVE_DEFAULT_DELIMITER:string = " ";
const ADJECTIVE_SEQUENCE_DELIMITER:string = ", ";
const ADJECTIVE_CONJUNCTION_DELIMITER:string = " and ";
const ADJECTIVE_DISJUNCTION_DELIMITER:string = " or ";

class EnglishAdjectiveDelimiterInstance extends OgmaHTMLBoundObject {
    private _parentAdjective: EnglishAdjectiveInstance;
    private _delimiterSpan: OgmaHTMLSpanElement;

    constructor(parentAdjective: EnglishAdjectiveInstance, delimiterSpan: OgmaHTMLSpanElement) {
        super();
        this._parentAdjective = parentAdjective;
        this._delimiterSpan = delimiterSpan;
        this.addOgmaHTMLElement(delimiterSpan);

        this._delimiterSpan.registerInterestInEventType("click");
    }

    get info() :string {
        let leftAdj: EnglishAdjectiveInstance = this._parentAdjective.previous as EnglishAdjectiveInstance;
        let leftAdjPriority: number = leftAdj.priority;
        let leftAdjCategoryName: string = leftAdj.adjective.getCategory().getName();
        let leftAdjDescription: string = "To the left, <b>" + leftAdj.getLexeme() + "</b> is " + leftAdj.typeDescription + " with precedence " + leftAdjPriority;
        let thisAdjPriority:number = this._parentAdjective.priority;
        let thisAdjCategoryName: string = this._parentAdjective.adjective.getCategory().getName();
        let thisAdjDescription: string = "To the right, <b>" + this._parentAdjective.getLexeme() + "</b> is " + this._parentAdjective.typeDescription + " with precedence " + thisAdjPriority;
        let currentDelimiter: string = this._parentAdjective.currentDelimiterText;
        let result: string = "<p>";

        result += leftAdjDescription + ". ";
        result += thisAdjDescription + ". ";
        result += "<br/>";

        if (leftAdjPriority !== thisAdjPriority) {
            // Should be default delimiter, but error check it.
            if (currentDelimiter.localeCompare(ADJECTIVE_DEFAULT_DELIMITER) == 0) {
                result += 'A space separates these two modifiers because they are of different precedences. If they were of the same precedence, they would be delimited by a comma or a conjunction, such as "and", "or", or "but".';
            } else {
                alert("The delimiter between adjectives does not match expectations.");
            }
        } else {
            let rightAdj: EnglishAdjectiveInstance = (this._parentAdjective.next !== null) ? this._parentAdjective.next as EnglishAdjectiveInstance : null;
            let rightAdjPriority: number = (rightAdj !== null) ? rightAdj.priority : Infinity;

            if ((leftAdjPriority === thisAdjPriority) && (thisAdjPriority === rightAdjPriority)) {
                // Should be a comma.
                if (currentDelimiter.localeCompare(ADJECTIVE_SEQUENCE_DELIMITER) == 0) {
                    result += 'A comma separates these two modifiers because they are of the same precedence. If they were of the different precedences, they would be delimited by a space.';
                } else {
                    alert("The delimiter between adjectives does not match expectations.");
                }
            } else if ((leftAdjPriority === thisAdjPriority) && (thisAdjPriority !== rightAdjPriority)) {
                // Should be a conjunction or disjunction.
                if (currentDelimiter.localeCompare(ADJECTIVE_CONJUNCTION_DELIMITER) == 0) {
                    result += 'The conjunctive "and" separates these two modifiers because they are the last two modifiers with this precedence in the modifier sequence.';
                } else if (currentDelimiter.localeCompare(ADJECTIVE_DISJUNCTION_DELIMITER) == 0) {
                    result += 'The conjunctive "or" separates these two modifiers because they are the last two modifiers with this precedence in the modifier sequence.';
                } else {
                    alert("The delimiter between adjectives does not match expectations.");
                }
            } else {
                result += "The modifier sequence is not in proper order. When the modifier sequence is not in proper order, Ogma delimits the modifiers with a space and strikes through the disorderly modifiers.";
            }
        }

        return result + "</p>";
    }

    ogmaHTMLElementListener(source: OgmaHTMLNode, eventType: string, data?: Object): void {
        if ("click".localeCompare(eventType) == 0) {
            let container: HTMLElement = document.getElementById(OgmaConfiguration.config.discoveryContainerID);

            if (container !== null) {
                container.innerHTML = this.info;
            }
        }
    }
}

class EnglishAdjectiveInstance extends OgmaDoubleLinkedListElement
    implements PartOfSpeechInstance, IOgmaHTMLBoundObject, OgmaDraggableObject {
    protected ogmaHTMLElements: ArrayList<OgmaHTMLElement>;
    protected _parentDroppableContainer: OgmaDroppableContainer;
    protected _droppableContainers: LinearSet<OgmaDroppableContainer>;
    protected _delimiter: EnglishAdjectiveDelimiterInstance;
    protected _myDiv: OgmaHTMLDivElement;
    protected _lexemeSpan: OgmaHTMLSpanElement;
    protected _delimiterSpan: OgmaHTMLSpanElement;
    protected _adjective: EnglishAdjective;
    protected _disharmonies: EnglishAdjectiveDisharmonies;
    protected _baseCSSClass: string = "adjectiveinstance";
    protected _cssSelectedSuffix: string = "-selected";
    protected _cssHarmoniousSuffix: string = "";
    protected _cssInharmoniousSuffix: string = "-inharmonious";
    protected _isSelected: boolean = false;
    protected _currentDelimiterText: string = ADJECTIVE_NULL_DELIMITER;

    constructor(adjective?: EnglishAdjective) {
        super();
        this.ogmaHTMLElements = new ArrayList<OgmaHTMLElement>();
        this._parentDroppableContainer = null;
        this._droppableContainers = new LinearSet<OgmaDroppableContainer>();
        this._adjective = (adjective === undefined) ? null : adjective;
        this._disharmonies = new EnglishAdjectiveDisharmonies(this);

        this._parentDroppableContainer = this._adjective;

        this.initHTML();
        this.initEvents();
    }

    protected initHTML(): void {
        this._myDiv = this.addOgmaHTMLElement(new OgmaHTMLDivElement(this)) as OgmaHTMLDivElement;
        this._myDiv.setAttributeValue("data-ogma", "EnglishAdjectiveInstance/myDiv");
        this._myDiv.setAttributeValue("draggable", "true");
        this._myDiv.display = "inline";

        this._lexemeSpan = this.addOgmaHTMLElement(new OgmaHTMLSpanElement(this)) as OgmaHTMLSpanElement;
        this._lexemeSpan.setAttributeValue("data-ogma", "EnglishAdjectiveInstance/myDiv/lexemeSpan");
        this._lexemeSpan.className = this.cssClass;
        this._lexemeSpan.registerInterestInEventType("click");
        this._lexemeSpan.registerInterestInEventType("mouseenter");
        this._lexemeSpan.registerInterestInEventType("mouseleave");

        this._delimiterSpan = this.addOgmaHTMLElement(new OgmaHTMLSpanElement()) as OgmaHTMLSpanElement;
        this._delimiterSpan.setAttributeValue("data-ogma", "EnglishAdjectiveInstance/myDiv/delimiterSpan");
        this._delimiterSpan.className = this.computeCssClass("delimiterinstance");
        this._delimiterSpan.display = "none";

        this._delimiter = new EnglishAdjectiveDelimiterInstance(this, this._delimiterSpan);

        this._myDiv.registerInterestInEventType("click");
        this._myDiv.registerInterestInEventType("mouseenter");
        this._myDiv.registerInterestInEventType("mouseleave");

        this._myDiv.appendChild(this._delimiterSpan);
        this._myDiv.appendChild(this._lexemeSpan);

        this._lexemeSpan.text = this.getLexeme();
        this._delimiterSpan.appendText(ADJECTIVE_NULL_DELIMITER);
        this._delimiterSpan.display = "none";
    }

    protected initEvents():void {
        let self: EnglishAdjectiveInstance = this;

        this._myDiv.htmlElement.ondragstart = function (e: Event) {
            console.log("EnglishAdjectiveInstance.ondragstart(): e=" + e);
            let dragEvent: OgmaHTMLDragEvent = new OgmaHTMLDragEvent(e);

            console.log("  dragEvent=" + dragEvent + ", dragEvent.dataTransfer=" + dragEvent.dataTransfer);

            dragEvent.dataTransfer.ogmaSource = self;
        }

        this._myDiv.htmlElement.ondrag = function (e:Event) {
            self.onDrag();
        }
    }

    getOgmaHTML(): OgmaHTMLElement {
        return this._myDiv;
    }

    getHTML(): HTMLDivElement {
        return this._myDiv.htmlElement as HTMLDivElement;
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
        console.log("  received " + eventType + " event from " + source.toString());

        if ("click".localeCompare(eventType) == 0) {
            let container: HTMLElement = document.getElementById(OgmaConfiguration.config.discoveryContainerID);

            if (container !== null) {
                container.innerHTML = this.info;
            }
        }
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

    parentChanged(): void {

        let nounInstance: EnglishNounInstance = null;

        if ((this.parent === undefined) || (this.parent === null)) {
            nounInstance = null;
        } else if (this.parent instanceof EnglishAdjectivalPhrase) {
            nounInstance = (this.parent as EnglishAdjectivalPhrase).getNoun();
        } else if (this.parent instanceof EnglishCumulativeAdjectiveInstance) {
            nounInstance = (this.parent as EnglishCumulativeAdjectiveInstance).getNoun();
        }

        this._disharmonies.clear();

        console.log("EnglishAdjectiveInstance.parentChanged(): " + this);
        console.log("  parent=" + this.parent);

        if ((this.parent !== undefined) && (this.parent !== null)) {
            let current: OgmaDoubleLinkedListElement = this.parent.head;

            while (current !== null) {
                if (current !== this) {
                    if (current instanceof EnglishAdjectiveInstance) {
                        if (this.conflicts(current)) {
                            this._disharmonies.add(current, EnglishAdjectiveDisharmoniousState.IN_CONFLICT);
                            break;
                        }
                    }
                }
                current = (current.next as OgmaDoubleLinkedListElement);
            }

            if (! this.appropriateForNoun(nounInstance)) {
                this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.INAPPROPRIATE);
            }
        }

        this.disorderly(this._disharmonies);

        this._lexemeSpan.className = this.cssClass;
        this._delimiterSpan.className = this.computeCssClass("delimiterinstance");
    }

    addDroppableContainer(droppable: OgmaDroppableContainer): void {
        if (droppable.applyForAcceptance(this)) {
            this._droppableContainers.add(droppable)
        }
    }

    addDroppableContainers(droppables: Array<OgmaDroppableContainer>): void {
        droppables.forEach(function (droppable) {
            this.addDroppableContainer(droppable);
        })
    }

    removeDroppableContainer(droppable: OgmaDroppableContainer): void {
        if (this._droppableContainers.contains(droppable))
            this._droppableContainers.remove(droppable);
    }

    removeAllDroppableContainers(): void {
        this._droppableContainers.clear();
    }

    getParentDroppableContainer(): OgmaDroppableContainer {
        return this._parentDroppableContainer;
    }

    onDrag(): void {

    }

    onDropped(droppable: OgmaDroppableContainer): void {
        console.log("EnglishAdjectiveInstance.onDropped(): droppable=" + droppable);
        if (OgmaConfiguration.config.autoApplyForDrop || (this._droppableContainers.contains(droppable))) {
            this._parentDroppableContainer.onDraggedFrom(this);
            this._parentDroppableContainer = droppable;
        }
    }

    toPartOfSpeechInstance(): PartOfSpeechInstance {
        return this;
    }

    get priority(): AdjectivalOrderPriority {
        if (this._adjective === null) {
            alert("Attempt to get the priority of orphaned adjective instance " + this.toString() + " failed.");
            return AdjectivalOrderPriority.OPINION;
        }

        return this._adjective.priority;
    }

    get rankInPriority(): number {
        if (this._adjective === null) {
            alert("Attempt to get the rank in priority of orphaned adjective instance " + this.toString() + " failed.");
            return 0;
        }

        return this._adjective.rankInPriority;
    }

    get adjective(): EnglishAdjective {
        return this._adjective;
    }

    getPlurality(): EnglishPlurality {
        return this._adjective.getPlurality();
    }

    getLexeme(): string {
        return this._adjective.getLexeme();
    }

    getGrammaticalNumber(): GrammaticalNumber {
        switch (this.getPlurality()) {
            case EnglishPlurality.SINGULAR_ONLY:
                return GrammaticalNumber.SINGULAR;
            case EnglishPlurality.PLURAL_ONLY:
                return GrammaticalNumber.PLURAL;
            case EnglishPlurality.SINGULAR_OR_PLURAL:
                return GrammaticalNumber.NONE;
            default:
                return GrammaticalNumber.NONE;
        }
    }

    getPartOfSpeechType(): PartOfSpeechType {
        return this._adjective.getType();
    }

    getPartOfSpeech(): PartOfSpeech {
        return this._adjective;
    }

    conflicts(adj: EnglishAdjectiveInstance): boolean {
        return this._adjective.conflicts(adj.adjective);
    }

    appropriateForNoun(n?: EnglishNounInstance): boolean {
        let noun: EnglishNounInstance;

        if ((n !== undefined) && (n !== null)) {
            noun = n;
        } else {
            if (this.parent instanceof EnglishNounInstance) {
                noun = this.parent as EnglishNounInstance;
            } else if (this.parent instanceof EnglishCumulativeAdjectiveInstance) {
                noun = (this.parent as EnglishCumulativeAdjectiveInstance).noun;
            }
        }

        if ((noun !== undefined) && (noun !== null))
            return this._adjective.appropriateForNoun(noun.noun);
        else {
            // If there is no noun, the the adjective is not inappropriate.
            return true;
        }
    }

    disorderly(disharmonies?: EnglishAdjectiveDisharmonies): boolean {
        let firstInList: boolean = false;
        let lastInList: boolean = false;
        let leftDisorderly:boolean = false;
        let rightDisorderly:boolean = false;
        let previousAdjInst: EnglishAdjectiveInstance = null;
        let nextAdjInst: EnglishAdjectiveInstance = null;
        let disorderly: boolean = false;

        if ((this.previous === undefined) || (this.previous === null)) {
            firstInList = true;
        }

        if ((this.next === undefined) || (this.next === null)) {
            lastInList = true;
        }

        if (! firstInList) {
            previousAdjInst = this.previous as EnglishAdjectiveInstance;
            leftDisorderly = previousAdjInst.priority > this.priority;

            if (leftDisorderly) {
                disharmonies.add(previousAdjInst, EnglishAdjectiveDisharmoniousState.DISORDERED, true);
            }
        }

        if (! lastInList) {
            nextAdjInst = this.next as EnglishAdjectiveInstance;
            rightDisorderly = nextAdjInst.priority < this.priority;

            if (rightDisorderly) {
                disharmonies.add(nextAdjInst, EnglishAdjectiveDisharmoniousState.DISORDERED, false);
            }
        }

        disorderly = leftDisorderly || rightDisorderly;

        if ((previousAdjInst !== null) && (nextAdjInst !== null)) {
            if (previousAdjInst.priority == this.priority) {
                if (!lastInList) {
                    if (nextAdjInst.priority == this.priority) {
                        this._delimiterSpan.textContent = ADJECTIVE_SEQUENCE_DELIMITER;
                        this._currentDelimiterText = ADJECTIVE_SEQUENCE_DELIMITER;
                    } else {
                        this._delimiterSpan.textContent = ADJECTIVE_CONJUNCTION_DELIMITER;
                        this._currentDelimiterText = ADJECTIVE_CONJUNCTION_DELIMITER;
                    }
                } else {
                    this._delimiterSpan.textContent = ADJECTIVE_CONJUNCTION_DELIMITER;
                    this._currentDelimiterText = ADJECTIVE_CONJUNCTION_DELIMITER;
                }
            } else {
                this._delimiterSpan.textContent = ADJECTIVE_DEFAULT_DELIMITER
                this._currentDelimiterText = ADJECTIVE_DEFAULT_DELIMITER;
            }
            this._delimiterSpan.display = "inline";
        } else if (previousAdjInst !== null) {
            if (previousAdjInst.priority == this.priority) {
                this._delimiterSpan.textContent = ADJECTIVE_CONJUNCTION_DELIMITER;
                this._currentDelimiterText = ADJECTIVE_CONJUNCTION_DELIMITER;
            } else {
                this._delimiterSpan.textContent = ADJECTIVE_DEFAULT_DELIMITER;
                this._currentDelimiterText = ADJECTIVE_DEFAULT_DELIMITER;
            }
            this._delimiterSpan.display = "inline";
        } else if (nextAdjInst !== null) {
            this._delimiterSpan.textContent = ADJECTIVE_NULL_DELIMITER;
            this._currentDelimiterText = ADJECTIVE_NULL_DELIMITER;
            this._delimiterSpan.display = "none";
        }

        return disorderly;
    }

    compareTo(other: OgmaObject): number {
        if (other instanceof EnglishAdjectiveInstance) {
            let myIndex: number = this.priority + (this.rankInPriority / 10000);
            let otherIndex: number = (other as EnglishAdjectiveInstance).priority + ((other as EnglishAdjectiveInstance).rankInPriority / 10000);

            if (myIndex == otherIndex)
                return 0;
            else if (myIndex < otherIndex) {
                return -1;
            } else
                return 1;
        } else {
            return super.compareTo(other);
        }
    }

    samePriority(other: EnglishAdjectiveInstance): boolean {
        return this.priority == other.priority;
    }

    get cssClass(): string {
        let harmonySuffix: string = (! this._disharmonies.isClear()) ? this._cssInharmoniousSuffix : this._cssHarmoniousSuffix;

        if (this._isSelected) {
            return this._baseCSSClass + harmonySuffix + this._cssSelectedSuffix;
        } else {
            return this._baseCSSClass + harmonySuffix;
        }
    }

    computeCssClass(base:string): string {
        return base;
    }

    public get typeDescription(): string {
        return "an adjective";
    }

    get currentDelimiterText(): string {
        return this._currentDelimiterText;
    }

    get info(): string {
        let myInfo: string = "<p>";

        myInfo += "<b>" + this.getLexeme() + "</b> is " + this.typeDescription + ". ";
        myInfo += "It belongs to the " + this._adjective.getCategory().getName().toLowerCase() + " adjectival category, ";
        myInfo += "which has a precedence of " + this._adjective.getCategory().getPriority() + " in the left to right ordering of adjectives.";
        myInfo += "</p>";

        if (! this._disharmonies.isClear()) {
            myInfo += "<p>In the current context, it has the following issues:</p>";
            myInfo += this._disharmonies.info;
        }

        return myInfo;
    }

    toString(): string {
        return "/" + this.getLexeme() + "/";
    }
}

class EnglishDeterminerInstance extends EnglishAdjectiveInstance {
    protected _determinerType: EnglishDeterminerType;

    constructor(adjective?: EnglishAdjective, type?: EnglishDeterminerType) {
        super(adjective);
        this._determinerType = type;
    }
}

class EnglishPredeterminerInstance extends EnglishDeterminerInstance {
    constructor(adjective?: EnglishAdjective) {
        super(adjective, EnglishDeterminerType.PREDETERMINER);
    }
}

class EnglishCentralDeterminerInstance extends EnglishDeterminerInstance {
    constructor(adjective?: EnglishAdjective) {
        super(adjective, EnglishDeterminerType.CENTRAL_DETERMINER);
    }

    parentChanged(): void {
        console.log("EnglishCentralDeterminerInstance.parentChanged()");
        this._disharmonies.clear();

        this.disorderly(this._disharmonies);

        this._lexemeSpan.className = this.cssClass;
        this._delimiterSpan.className = this.computeCssClass("delimiterinstance");
    }

    disorderly(disharmonies?: EnglishAdjectiveDisharmonies): boolean {
        console.log("EnglishCentralDeterminerInstance.disorderly():");
        if ((this.parent === undefined) || (this.parent === null)) return true;

        let disorderly: boolean = false;

        if (! (this instanceof EnglishDemonstrativeInstance)) {
            let otherCentralDeterminers: Array<EnglishAdjectiveInstance> = new Array<EnglishAdjectiveInstance>();
            let currentSibling: EnglishAdjectiveInstance = this.parent.head as EnglishAdjectiveInstance;

            while (currentSibling !== null) {
                if (currentSibling !== this) {
                    console.log("  checking " + currentSibling.toString());
                    if (currentSibling instanceof EnglishCentralDeterminerInstance) {
                        console.log("  " + currentSibling.toString() + " is a central determiner");
                        otherCentralDeterminers.push(currentSibling);
                        disharmonies.add(currentSibling, EnglishAdjectiveDisharmoniousState.MORE_THAN_ONE_CENTRAL_DETERMINER);
                        disorderly = true;
                    }
                }
                currentSibling = currentSibling.next as EnglishAdjectiveInstance;
            }
        }

        disorderly = super.disorderly(disharmonies) || disorderly;

        return disorderly;
    }
}

class EnglishPostDeterminerInstance extends EnglishDeterminerInstance {
    constructor(adjective?: EnglishAdjective) {
        super(adjective, EnglishDeterminerType.POST_DETERMINER);
    }
}

class EnglishArticleInstance extends EnglishCentralDeterminerInstance {
    constructor(adjective?: EnglishAdjective) {
        super(adjective);
    }

    parentChanged(): void {
        console.log("EnglishArticleInstance.parentChanged(): " + this);
        console.log("  parent: " + this.parent);
        this._disharmonies.clear();

        let nounInstance: EnglishNounInstance = null;

        if ((this.parent === undefined) || (this.parent === null)) {
            nounInstance = null;
        } else if (this.parent instanceof EnglishAdjectivalPhrase) {
            nounInstance = (this.parent as EnglishAdjectivalPhrase).getNoun();
        } else if (this.parent instanceof EnglishCumulativeAdjectiveInstance) {
            nounInstance = (this.parent as EnglishCumulativeAdjectiveInstance).getNoun();
        }

        console.log("  nounInstance: " + nounInstance);

        if (nounInstance === undefined) {
            nounInstance = null;
        }

        let myLexeme: string = this.getLexeme().toLowerCase();
        let definiteArticle: boolean = false;
        let indefiniteAn: boolean = false;

        if (myLexeme.localeCompare("an") == 0) {
            indefiniteAn = true;
        } else if (myLexeme.localeCompare("a") == 0) {
            indefiniteAn = false;
        } else if (myLexeme.localeCompare("the") == 0) {
            definiteArticle = true;
        }

        let nextPartOfSpeech: any = (this.next == null) ? nounInstance : this.next;

        if (! definiteArticle) {
            if (nextPartOfSpeech !== null) {
                if ((nextPartOfSpeech instanceof EnglishAdjectiveInstance) || (nextPartOfSpeech instanceof EnglishNounInstance)) {
                    console.log("*** next is an adjective or noun");
                    // @ts-ignore
                    let sibling: PartOfSpeechInstance = nextPartOfSpeech as PartOfSpeechInstance;
                    let siblingLexeme: string = sibling.getLexeme();
                    let siblingLexemeBeginsWithVowel: boolean = beginsWithOneOf(siblingLexeme, "aeiou");

                    console.log("*** siblingLexemeBeginsWithVowel=" + siblingLexemeBeginsWithVowel);

                    if (indefiniteAn && !siblingLexemeBeginsWithVowel) {
                        this._disharmonies.add(sibling, EnglishAdjectiveDisharmoniousState.AN_BEFORE_CONSONANT);
                    } else if (!indefiniteAn && siblingLexemeBeginsWithVowel) {
                        this._disharmonies.add(sibling, EnglishAdjectiveDisharmoniousState.A_BEFORE_VOWEL);
                    }
                }
            }
        }

        if (nounInstance !== null) {
            //!!!! Handle misuse of determiner in relation to noun.
            if (nounInstance.nounType == NounType.PROPER) {
                this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.ARTICLE_MODIFYING_PROPER_NOUN);
            }

            if (nounInstance.countability == NounCountabilityType.UNCOUNTABLE) {
                if (! definiteArticle) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.INDEFINITE_ARTICLE_MODIFYING_UNCOUNTABLE_NOUN);
                }
            }

            if (nounInstance.alwaysPlural) {
                if (this.getPlurality() === EnglishPlurality.SINGULAR_ONLY) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            } else if (nounInstance.getGrammaticalNumber() === GrammaticalNumber.SINGULAR) {
                if ((this.getPlurality() !== EnglishPlurality.SINGULAR_OR_PLURAL)
                    && (this.getPlurality() !== EnglishPlurality.SINGULAR_ONLY)) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            } else if (nounInstance.getGrammaticalNumber() === GrammaticalNumber.PLURAL) {
                if (this.getPlurality() === EnglishPlurality.SINGULAR_ONLY) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            }
        }

        this.disorderly(this._disharmonies);

        this._lexemeSpan.className = this.cssClass;
        this._delimiterSpan.className = this.computeCssClass("delimiterinstance");
    }

    public get typeDescription(): string {
        return "an article";
    }
}

class EnglishDemonstrativeInstance extends EnglishCentralDeterminerInstance {
    constructor(adjective?: EnglishAdjective) {
        super(adjective);
    }

    public get typeDescription(): string {
        return "a demonstrative";
    }

    parentChanged(): void {
        console.log("EnglishDemonstrativeInstance.parentChanged(): " + this);
        console.log("  parent: " + this.parent);
        this._disharmonies.clear();

        let nounInstance: EnglishNounInstance = null;

        if ((this.parent === undefined) || (this.parent === null)) {
            nounInstance = null;
        } else if (this.parent instanceof EnglishAdjectivalPhrase) {
            nounInstance = (this.parent as EnglishAdjectivalPhrase).getNoun();
        } else if (this.parent instanceof EnglishCumulativeAdjectiveInstance) {
            nounInstance = (this.parent as EnglishCumulativeAdjectiveInstance).getNoun();
        }

        if (nounInstance !== null) {
            if (nounInstance.alwaysPlural) {
                if (this.getPlurality() === EnglishPlurality.SINGULAR_ONLY) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            } else if (nounInstance.getGrammaticalNumber() === GrammaticalNumber.SINGULAR) {
                if ((this.getPlurality() !== EnglishPlurality.SINGULAR_OR_PLURAL)
                    && (this.getPlurality() !== EnglishPlurality.SINGULAR_ONLY)) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            } else if (nounInstance.getGrammaticalNumber() === GrammaticalNumber.PLURAL) {
                if (this.getPlurality() === EnglishPlurality.SINGULAR_ONLY) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            }
        }

        this.disorderly(this._disharmonies);

        this._lexemeSpan.className = this.cssClass;
        this._delimiterSpan.className = this.computeCssClass("delimiterinstance");
    }
}

class EnglishPossessiveInstance extends EnglishCentralDeterminerInstance {
    constructor(adjective?: EnglishAdjective) {
        super(adjective);
    }

    public get typeDescription(): string {
        return "a possessive";
    }
}

class EnglishQuantifierInstance extends EnglishPostDeterminerInstance {
    constructor(adjective?: EnglishAdjective) {
        super(adjective);
    }

    public get typeDescription(): string {
        return "a quantifier";
    }

    parentChanged(): void {
        console.log("EnglishQuantifierInstance.parentChanged(): " + this);
        console.log("  parent: " + this.parent);
        this._disharmonies.clear();

        let nounInstance: EnglishNounInstance = null;

        if ((this.parent === undefined) || (this.parent === null)) {
            nounInstance = null;
        } else if (this.parent instanceof EnglishAdjectivalPhrase) {
            nounInstance = (this.parent as EnglishAdjectivalPhrase).getNoun();
        } else if (this.parent instanceof EnglishCumulativeAdjectiveInstance) {
            nounInstance = (this.parent as EnglishCumulativeAdjectiveInstance).getNoun();
        }

        if (nounInstance !== null) {
            if (nounInstance.alwaysPlural) {
                if (this.getPlurality() === EnglishPlurality.SINGULAR_ONLY) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            } else if (nounInstance.getGrammaticalNumber() === GrammaticalNumber.SINGULAR) {
                if ((this.getPlurality() !== EnglishPlurality.SINGULAR_OR_PLURAL)
                    && (this.getPlurality() !== EnglishPlurality.SINGULAR_ONLY)) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            } else if (nounInstance.getGrammaticalNumber() === GrammaticalNumber.PLURAL) {
                if (this.getPlurality() === EnglishPlurality.SINGULAR_ONLY) {
                    this._disharmonies.add(nounInstance, EnglishAdjectiveDisharmoniousState.MISMATCH_IN_NUMBER);
                }
            }
        }

        this.disorderly(this._disharmonies);

        this._lexemeSpan.className = this.cssClass;
        this._delimiterSpan.className = this.computeCssClass("delimiterinstance");
    }
}

interface OgmaDraggableObject {
    addDroppableContainer(droppable: OgmaDroppableContainer): void;
    addDroppableContainers(droppables: Array<OgmaDroppableContainer>): void;
    getParentDroppableContainer(): OgmaDroppableContainer;
    removeDroppableContainer(droppable: OgmaDroppableContainer): void;
    removeAllDroppableContainers(): void;
    onDrag(): void;
    onDropped(droppable: OgmaDroppableContainer) :void;
    toPartOfSpeechInstance(): PartOfSpeechInstance;
}

interface OgmaDroppableContainer {
    applyForAcceptance(draggable: OgmaDraggableObject): boolean;
    acceptsDrop(draggable: OgmaDraggableObject): boolean;
    onDrop(event: OgmaHTMLDragEvent): boolean;
    onDraggedFrom(draggable: OgmaDraggableObject): void;
    toWordSequence(): WordSequence;
}

interface EnglishNounModifier {
    getNoun(): EnglishNounInstance;
    modifiesNoun(): boolean;
}

class WordChunkInstance<T extends OgmaDoubleLinkedListElement>
    extends OgmaDoubleLinkedList<T> implements OgmaDroppableContainer {
    protected ogmaHTMLElements: ArrayList<OgmaHTMLElement>;
    protected _myDiv: OgmaHTMLDivElement;
    protected _noopDropHandler: (e: Event) => void;
    protected _acceptDropHandler: (e: Event) => void;

    constructor() {
        super();
        let self = this;
        this.ogmaHTMLElements = new ArrayList<OgmaHTMLElement>();
        this._noopDropHandler = function (e:Event) { console.log("** noop ***")};
        this.initAcceptDropHandler();
        this.initHTML();
    }

    protected initAcceptDropHandler(): void {
        let self = this;
        this._acceptDropHandler = function (e:Event) {
            console.log("*** WordChunkInstance.acceptDropHandler ***");
            e.preventDefault();
            self.onDrop(new OgmaHTMLDragEvent(e));
        };
    }

    protected initHTML(): void {
        this._myDiv = this.addOgmaHTMLElement(new OgmaHTMLDivElement(this)) as OgmaHTMLDivElement;
        this._myDiv.setAttributeValue("data-ogma", "WordChunkInstance/myDiv");
        this._myDiv.display = "inline";

        if (OgmaConfiguration.config.autoApplyForDrop) {
            this._myDiv.htmlElement.ondrop = this._acceptDropHandler;
            this._myDiv.htmlElement.ondragover = function (e:any) {
                console.log("*** WordChunkInstance.ondragover ***");
                e.preventDefault();
                e.dataTransfer.dropEffect = "move";
            };
        } else {
            this._myDiv.htmlElement.ondrop = this._noopDropHandler;
            this._myDiv.htmlElement.ondragover = this._noopDropHandler;
        }
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

    parentChanged(): void {
    }

    add(element: T): OgmaDoubleLinkedList<T> {
        super.add(element);

        return this as OgmaDoubleLinkedList<T>;
    }

    applyForAcceptance(draggable: OgmaDraggableObject): boolean {
        return false;
    }

    acceptsDrop(draggable: OgmaDraggableObject): boolean {
        return false;
    }

    protected onDropSourceAndDestination(source: OgmaObject, destination: OgmaObject): boolean {
        return false;
    }

    onDrop(event: OgmaHTMLDragEvent): boolean {
        let dt: OgmaHTMLDataTransfer = event.dataTransfer;
        let source: OgmaObject = dt.ogmaSource;
        let destination: OgmaObject = dt.ogmaDestination;

        let result = this.onDropSourceAndDestination(source, destination);

        // @ts-ignore
        (source as OgmaDraggableObject).onDropped(this);

        console.log("WordChunkInstance.onDrop(): source=" + source + ", destination=" + destination);
        console.log("  After Drop: " + this.toString());

        return result;
    }

    onDraggedFrom(draggable: OgmaDraggableObject): void {

    }

    toWordSequence(): WordSequence {
        return null;
    }
}

class EnglishCumulativeAdjectiveInstance
    extends WordChunkInstance<EnglishAdjectiveInstance>
    implements WordSequence, EnglishNounModifier, IOgmaHTMLBoundObject {
    protected _acceptedAdjectiveInstances: LinearSet<EnglishAdjectiveInstance>;
    protected _noun: EnglishNounInstance;

    constructor(noun?: EnglishNounInstance) {
        super();
        this._acceptedAdjectiveInstances = new LinearSet<EnglishAdjectiveInstance>();
        this._noun = (noun === undefined) ? null : noun;
    }

    protected initAcceptDropHandler(): void {
        let self = this;
        this._acceptDropHandler = function (e:Event) {
            console.log("*** EnglishCumulativeAdjectiveInstance.acceptDropHandler ***");
            let ogmaEvent: OgmaHTMLDragEvent = new OgmaHTMLDragEvent(e);
            if (OgmaConfiguration.config.autoApplyForDrop || (self._acceptedAdjectiveInstances.contains(ogmaEvent.ogmaSource as EnglishAdjectiveInstance))) {
                e.preventDefault();
                self.onDrop(ogmaEvent);
            } else {
                self._noopDropHandler(e);
            }
        };
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

    parentChanged(): void {
        this.informMembersOfChange();
    }

    get adjectivesDiv(): OgmaHTMLDivElement {
        return this._myDiv;
    }

    add(element: EnglishAdjectiveInstance): OgmaDoubleLinkedList<EnglishAdjectiveInstance> {
        console.log("EnglishCumulativeAdjectiveInstance.add(): element=" + element);

        let result: OgmaDoubleLinkedList<EnglishAdjectiveInstance> = super.add(element);

        let div: OgmaHTMLDivElement = this.adjectivesDiv;

        if (OgmaConfiguration.config.reorderAdjectives) {
            this.sort(EnglishAdjectiveInstanceComparator.comparator);

            let children: Array<OgmaHTMLObject> = div.children.toArray();
            let count: number = children.length;

            for (let i: number = 0; i < count; i++) {
                div.removeChild(children[i]);
            }

            let current: EnglishAdjectiveInstance = this.head;

            while (current != null) {
                div.appendChild(current.getOgmaHTML());
                current = current.next as EnglishAdjectiveInstance;
            }

            div.appendChild(element.getOgmaHTML());
        } else {
            div.appendChild(element.getOgmaHTML());
        }

        return result;
    }

    // @ts-ignore
    remove(element: EnglishAdjectiveInstance): EnglishCumulativeAdjectiveInstance {
        super.remove(element);
        this.adjectivesDiv.removeChild(element.getOgmaHTML());
        return this;
    }

    getNoun(): EnglishNounInstance {
        if (this._noun === undefined)
            return null;
        else {
            return this._noun;
        }
    }

    get noun(): EnglishNounInstance {
        if (this._noun === undefined)
            return null;
        else {
            return this._noun;
        }
    }

    set noun(n: EnglishNounInstance) {
        if (n === undefined)
            this._noun = null;
        else
            this._noun = n;
    }

    modifiesNoun(): boolean {
        return (this._noun !== undefined) && (this._noun !== null);
    }

    applyForAcceptance(draggable: OgmaDraggableObject): boolean {
        if (draggable instanceof EnglishAdjectiveInstance) {
            this._acceptedAdjectiveInstances.add(draggable as EnglishAdjectiveInstance);
            this._myDiv.htmlElement.ondrop = this._acceptDropHandler;
            return true;
        }

        return false;
    }

    acceptsDrop(draggable: OgmaDraggableObject): boolean {
        if (draggable instanceof EnglishAdjectiveInstance) {
            return (OgmaConfiguration.config.autoApplyForDrop || this._acceptedAdjectiveInstances.contains(draggable as EnglishAdjectiveInstance));
        }

        return false;
    }

    protected onDropSourceAndDestination(source: OgmaObject, destination: OgmaObject): boolean {
        console.log("EnglishCumulativeAdjectiveInstance.onDropSourceAndDestination");
        if (destination !== this as OgmaObject) return false;

        let adjSource: EnglishAdjectiveInstance = null;

        if (source instanceof EnglishAdjectiveInstance) {
            adjSource = source as EnglishAdjectiveInstance;
        } else {
            return false;
        }

        if (adjSource === null) return false;

        if (this.isMember(adjSource)) return false;

        (source as OgmaDraggableObject).getParentDroppableContainer().onDraggedFrom((source as OgmaDraggableObject));

        this.add(adjSource);

        return false;
    }

    onDraggedFrom(draggable: OgmaDraggableObject): void {

    }

    toWordSequence(): WordSequence {

        return this;
    }
}

class EnglishAdjectivalPhrase extends EnglishCumulativeAdjectiveInstance {
    protected _adjsDiv: OgmaHTMLDivElement;
    protected _nounDiv: OgmaHTMLDivElement;

    constructor(noun?: EnglishNounInstance) {
        super(noun);
        this.initHTML2();

        if ((noun !== undefined) && (noun !== null)) {
            noun.phrasalParent = this;
        }
    }

    protected initAcceptDropHandler(): void {
        console.log("*** EnglishAdjectivalPhrase.initAcceptDropHandler ***");
        let self = this;
        this._acceptDropHandler = function (e:Event) {
            console.log("** EnglishAdjectivalPhrase.acceptDropHandler **");
            let ogmaEvent: OgmaHTMLDragEvent = new OgmaHTMLDragEvent(e);
            if (OgmaConfiguration.config.autoApplyForDrop || (self._acceptedAdjectiveInstances.contains(ogmaEvent.ogmaSource as EnglishAdjectiveInstance))) {
                e.preventDefault();
                self.onDrop(ogmaEvent);
            } else {
                self._noopDropHandler(e);
            }
        };
    }

    protected initHTML2(): void {
        this._myDiv.setAttributeValue("data-ogma", "EnglishAdjectivalPhrase/myDiv");

        this._adjsDiv = this.addOgmaHTMLElement(new OgmaHTMLDivElement(this)) as OgmaHTMLDivElement;
        this._adjsDiv.setAttributeValue("data-ogma", "EnglishAdjectivalPhrase/myDiv/adjsDiv");

        this._nounDiv = this.addOgmaHTMLElement(new OgmaHTMLDivElement(this)) as OgmaHTMLDivElement;
        this._nounDiv.setAttributeValue("data-ogma", "EnglishAdjectivalPhrase/myDiv/nounDiv");

        this._adjsDiv.display = "inline";
        this._nounDiv.display = "inline";

        this._myDiv.appendChild(this._adjsDiv);
        this._myDiv.appendChild(this._nounDiv);

        if (this._noun !== null) {
            this._nounDiv.appendChild(this.noun.getOgmaHTML());
        }

        this._myDiv.htmlElement.style.minWidth = "200px";
        this._myDiv.htmlElement.style.minHeight = "100px";
    }

    getHTML(): HTMLDivElement {
        return this._myDiv.htmlElement as HTMLDivElement;
    }

    get adjectivesDiv(): OgmaHTMLDivElement {
        return this._adjsDiv;
    }

    set noun(n: EnglishNounInstance) {
        console.log("Current noun is: " + this._noun);
        console.log("Setting noun to " + n);

        this.clearOgmaHTMLElements();
        this._nounDiv.removeAllChildren();

        if (n === undefined) {
            this._noun = null;
        } else if (n !== null) {
            this._noun = n;
            this._noun.phrasalParent = this;
            this._nounDiv.appendChild(n.getOgmaHTML());
            this.addOgmaHTMLElement(n.getOgmaHTML());
            // this._noun.noun.dealloc(this._noun);
        } else {
            this._noun = null;
        }

        this.informMembersOfChange();
    }

    protected onDropSourceAndDestination(source: OgmaObject, destination: OgmaObject): boolean {

        let adjSource: EnglishAdjectiveInstance = null;
        let nounSource: EnglishNounInstance = null;
        // @ts-ignore
        let type: PartOfSpeechType = (source as PartOfSpeechInstance).getPartOfSpeechType();

        console.log("*** type=" + type);

        if (source instanceof EnglishAdjectiveInstance) {
            console.log("  source is an instance of EnglishAdjectiveInstance");
            adjSource = source as EnglishAdjectiveInstance;
        } else if (source instanceof EnglishNounInstance) {
            console.log("  source is an instance of EnglishNounInstance");
            nounSource = source as EnglishNounInstance;
        } else {
            return false;
        }

        if (adjSource !== null) {
            if (this.isMember(adjSource)) {
                console.log("Adjective instance " + adjSource + " is already a member of this EnglishAdjectivalPhrase.");
                return false;
            }

            this.add(adjSource);
        } else if (nounSource !== null) {
            if (this._noun !== nounSource) {
                this.noun = nounSource;
                nounSource.phrasalParent = this;
                this.parentChanged();
                this.informMembersOfChange();
            }
        }

        console.log("EnglishAdjectivalPhrase.onDropSourceAndDestination(): source=" + source + ", destination=" + destination);
        console.log("  After Drop: " + this);

        return false;
    }

    toString(): string {
        return super.toString() + ":" + this._noun;
    }
}

/* UI Tools */

class OgmaRecyleBin implements OgmaDroppableContainer {
    private _parent: HTMLElement;
    private _myDiv: OgmaHTMLDivElement;
    private _canvas: HTMLElement;
    private _context: any;
    private _image: HTMLImageElement;
    private _imageWidth: number;
    private _imageHeight: number;
    private _grey: number = 0xC0C0C0;

    constructor(divID: string, canvasID: string, imagePath: string, imageWidth: number, imageHeight: number) {
        let self: OgmaRecyleBin = this;
        this._parent = document.getElementById(divID) as HTMLDivElement;
        this._canvas = document.getElementById(canvasID);

        // @ts-ignore
        this._context = this._canvas.getContext("2d");
        this._context.font = "12px Arial";
        this._context.fontWeight = "bold";
        this._context.fillStyle = this.nextGrey;

        let image: HTMLImageElement = new Image(imageWidth, imageHeight);
        this._image = image;
        this._image.src = imagePath;
        this._image.onload = function () {
            self._context.drawImage(image, 0, 0);
        };

        this._imageWidth = imageWidth;
        this._imageHeight = imageHeight;

        this.initHTML();

        console.log("OgmaRecyleBin(): this._myDiv=" + this._myDiv);
        console.log("OgmaRecyleBin(): this._image=" + this._image);
    }

    private initHTML(): void {
        let self: OgmaRecyleBin = this;

        this._myDiv = new OgmaHTMLDivElement();
        this._parent.appendChild(this._myDiv.htmlElement);
        this._myDiv.htmlElement.appendChild(this._canvas);

        this._myDiv.htmlElement.ondragover = function (e:any) {
            e.preventDefault();
            e.dataTransfer.dropEffect = "move";
        }

        this._myDiv.htmlElement.ondrop = function (e:any) {
            console.log("this._myDiv.ondrop()");
            e.preventDefault();
            self.onDrop(new OgmaHTMLDragEvent(e));
        }
    }

    private get nextGrey(): string {
        let result: string = "#" + this._grey.toString(16);

        if (this._grey + 0x010101 <= 0xFFFFFF) {
            this._grey += 0x010101;
        }

        return result;
    }

    private writeTextInBin(text: string, rotation?:number) {
        let context: any = this._context;
        let textInfo: TextMetrics = context.measureText(text);
        let textWidth: number = textInfo.width;
        let textHeight: number = (textInfo.actualBoundingBoxAscent + textInfo.actualBoundingBoxDescent);
        let centerX: number = this._imageWidth / 2;
        let centerY: number = this._imageHeight / 2;
        let randomDeviationFromCenterX: number = (Math.random() <= 0.3) ? centerX : centerX + 30 - Math.random() * 60;
        let randomDeviationFromCenterY: number = (Math.random() <= 0.3) ? centerY : centerY + 30 - Math.random() * 60;
        let radians: number = (rotation !== undefined) ? rotation * Math.PI / 180 : 0;

        context.fillStyle = this.nextGrey;

        context.save();
        context.translate(centerX, centerY);
        context.rotate(radians);
        context.translate(-centerX, -centerY);
        context.fillText(text, randomDeviationFromCenterX - textWidth / 2, randomDeviationFromCenterY - textHeight / 2, textWidth);
        context.restore();
    }

    private writeTextInBinWithRandomRotation(text: string) {
        let randomAngle: number = Math.floor(Math.random() * 360);
        this.writeTextInBin(text, randomAngle);
    }

    applyForAcceptance(draggable: OgmaDraggableObject): boolean {
        return true;
    }

    acceptsDrop(draggable: OgmaDraggableObject): boolean {
        return true;
    }

    onDrop(event: OgmaHTMLDragEvent): boolean {
        let source: OgmaObject = event.dataTransfer.ogmaSource;

        console.log("***** onDrop(): source=" + source);

        if (source == null) return;

        // @ts-ignore
        source.onDropped(this as OgmaDraggableObject);

        if (source instanceof EnglishNounInstance) {
            let nounInstance: EnglishNounInstance = source as EnglishNounInstance;

            console.log("Phrasal parent of " + nounInstance + " is " + nounInstance.phrasalParent);

            if (nounInstance.phrasalParent !== null) {
                if (nounInstance.phrasalParent instanceof EnglishAdjectivalPhrase) {
                    console.log("***** CLEARING NOUN *****");

                    (nounInstance.phrasalParent as EnglishAdjectivalPhrase).onDraggedFrom((source as OgmaDraggableObject));
                    (nounInstance.phrasalParent as EnglishAdjectivalPhrase).noun = null;
                }
            }

            this.writeTextInBinWithRandomRotation(nounInstance.getLexeme());

            (source as OgmaDraggableObject).getParentDroppableContainer().onDraggedFrom((source as OgmaDraggableObject));
        } else if (source instanceof EnglishAdjectiveInstance) {
            let adjectiveInstance: EnglishAdjectiveInstance = source as EnglishAdjectiveInstance;

            if (adjectiveInstance.parent !== null) {
                if (adjectiveInstance.parent instanceof EnglishCumulativeAdjectiveInstance) {
                    let adjectiveInstanceParent: EnglishCumulativeAdjectiveInstance = adjectiveInstance.parent as EnglishCumulativeAdjectiveInstance;

                    adjectiveInstanceParent.remove(adjectiveInstance);
                }
            }

            this.writeTextInBinWithRandomRotation(adjectiveInstance.getLexeme());

            (source as OgmaDraggableObject).getParentDroppableContainer().onDraggedFrom((source as OgmaDraggableObject));
        }

        return true;
    }

    onDraggedFrom(draggable: OgmaDraggableObject): void {
    }

    toWordSequence(): WordSequence {
        return null;
    }
}


/* Ogma Model */


abstract class OgmaModel {
    buildModel(): boolean {
        return true;
    }

    abstract getAdjectives(): Array<Adjective>;
    abstract getNouns(): Array<Noun>;
    abstract getAdjectiveCategory(catIdentifier:any): AdjectiveCategory;
    abstract getAdjectiveCategories(): Array<AdjectiveCategory>;
}

class EnglishOgmaModel extends OgmaModel {
    _ogmaAdjectives: Array<any>;
    _ogmaNouns: Array<any>;
    _ogmaCategories: Array<any>;

    _englishAdjectives: Array<EnglishAdjective>;
    _englishNouns: Array<EnglishNoun>;
    _englishAdjectiveCategories: Array<EnglishAdjectiveCategory>;

    constructor(ogma:any) {
        super();
        this._ogmaAdjectives = ogma.getAdjectives();
        this._ogmaNouns = ogma.getNouns();
        this._ogmaCategories = ogma.getCategories();

        this._englishAdjectives = new Array<EnglishAdjective>();
        this._englishNouns = new Array<EnglishNoun>();
        this._englishAdjectiveCategories = new Array<EnglishAdjectiveCategory>();
    }

    getAdjectives(): Array<Adjective> {
        return this._englishAdjectives as Array<Adjective>;
    }

    getNouns(): Array<Noun> {
        return this._englishNouns as Array<Noun>;
    }

    getAdjectiveCategory(catIdentifier:any): AdjectiveCategory {
        let count: number = this._englishAdjectiveCategories.length;
        let i: number;

        if (typeof catIdentifier == "string") {
            let name: string = (catIdentifier as string).toUpperCase();

            for (i = 0; i < count; i++) {
                if (this._englishAdjectiveCategories[i].name.toUpperCase().localeCompare(name) == 0) {
                    return this._englishAdjectiveCategories[i];
                }
            }
        } else {
            let priority: AdjectivalOrderPriority = catIdentifier as AdjectivalOrderPriority;

            for (i = 0; i < count; i++) {
                if (this._englishAdjectiveCategories[i].priority == priority) {
                    return this._englishAdjectiveCategories[i];
                }
            }
        }

        return null;
    }

    getAdjectiveCategories(): Array<AdjectiveCategory> {
        return this._englishAdjectiveCategories as Array<AdjectiveCategory>;
    }

    protected buildCategories(): void {
        let highestPriority: number = AdjectivalOrderPriority.PURPOSE.valueOf();

        for (let priorityAsInt: number = 0; priorityAsInt <= highestPriority; priorityAsInt++) {
            let priorityAsString:string = (AdjectivalOrderPriority as Object)[priorityAsInt] as string;
            let priority: AdjectivalOrderPriority = (AdjectivalOrderPriority as Object)[priorityAsString];
            let exclusive: boolean = false;

            switch (priority) {
                case AdjectivalOrderPriority.ARTICLE:
                    exclusive = true;
            }

            this._englishAdjectiveCategories.push(new EnglishAdjectiveCategory(priority, priorityAsString, exclusive));
        }
    }

    protected buildAdjectives(): void {
        let adjCount: number = this._ogmaAdjectives.length;

        for (let i: number = 0; i < adjCount; i++) {
            let ogmaAdjective: any = this._ogmaAdjectives[i];
            let precedence: AdjectivalOrderPriority = ogmaAdjective.getPrecedence() as number;
            let ogmaType: string = ogmaAdjective.getType();
            let ogmaPlurality: string = ogmaAdjective.getPlurality();
            let rank: number = ogmaAdjective.getRankInCategory() as number;
            let adjType: AdjectiveType;
            let plurality: EnglishPlurality;

            console.log("*** ogmaType=" + ogmaType + " ***");

            if (ogmaType.localeCompare("generic") == 0) {
                adjType = AdjectiveType.ADJECTIVE;
            } else if (ogmaType.localeCompare("article") == 0) {
                adjType = AdjectiveType.ARTICLE;
            } else if (ogmaType.localeCompare("demonstrative") == 0) {
                adjType = AdjectiveType.DEMONSTRATIVE;
            } else if (ogmaType.localeCompare("determiner") == 0) {
                adjType = AdjectiveType.DETERMINER;
            } else if (ogmaType.localeCompare("possessive") == 0) {
                adjType = AdjectiveType.POSSESSIVE;
            } else if (ogmaType.localeCompare("quantifier") == 0) {
                adjType = AdjectiveType.QUANTIFIER;
            } else {
                alert("Unknown adjective type: " + ogmaType);
                adjType = AdjectiveType.ADJECTIVE;
            }

            if (ogmaPlurality.localeCompare("unknown") == 0) {
                plurality = EnglishPlurality.UNKNOWN_PLURALITY;
            } else if (ogmaPlurality.localeCompare("singular") == 0) {
                plurality = EnglishPlurality.SINGULAR_ONLY;
            } else if (ogmaPlurality.localeCompare("plural") == 0) {
                plurality = EnglishPlurality.PLURAL_ONLY;
            } else if (ogmaPlurality.localeCompare("singular or plural") == 0) {
                plurality = EnglishPlurality.SINGULAR_OR_PLURAL;
            } else {
                plurality = EnglishPlurality.SINGULAR_OR_PLURAL;
            }

            let englishAdjective: EnglishAdjective
                = new EnglishAdjective(ogmaAdjective.getLexeme() as string, adjType, precedence, rank, plurality);

            this._englishAdjectives.push(englishAdjective);
        }
    }

    protected buildNouns(): void {
        let nounCount: number = this._ogmaNouns.length;

        for (let i: number = 0; i < nounCount; i++) {
            let ogmaNoun: any = this._ogmaNouns[i];
            let lexeme:string = ogmaNoun.getLexeme() as string;
            let ogmaGender: string = ogmaNoun.getGender() as string;
            let gender: GrammaticalGender;
            let nounCountabilityType: NounCountabilityType;
            let nounType: NounType;
            let plural: string = ogmaNoun.getPlural() as string;
            let isAlwaysPlural: boolean = ogmaNoun.isAlwaysPlural() as boolean;

            switch (ogmaGender) {
                case "genderless": gender = GrammaticalGender.UNKNOWN; break;
                case "masculine": gender = GrammaticalGender.MASCULINE; break;
                case "feminine": gender = GrammaticalGender.FEMININE; break;
                case "neuter": gender = GrammaticalGender.NEUTER; break;
                default: gender = GrammaticalGender.UNKNOWN; break;
            }

            if (ogmaNoun.isCountable()) {
                nounCountabilityType = NounCountabilityType.COUNTABLE
            } else {
                nounCountabilityType = NounCountabilityType.UNCOUNTABLE;
            }

            if (ogmaNoun.isProper()) {
                nounType = NounType.PROPER;
            } else {
                // This needs to be fixed. In the GrammarManager, only PROPER and CONCRETE are sent
                // to the JS file. All noun types must be sent. !!!
                nounType = NounType.CONCRETE;
            }

            this._englishNouns.push(new EnglishNoun(lexeme, nounType, gender, nounCountabilityType, plural, isAlwaysPlural));
        }
    }

    protected link(): void {
        let ogmaAdj: any;
        let adj: EnglishAdjective;
        let adjIndices: Array<number>;
        let outerCount: number = this._englishAdjectiveCategories.length;
        let innerCount: number;
        let i: number;
        let j: number;

        for (i = 0; i < outerCount; i++) {
            let ogmaCat: any = this._ogmaCategories[i] as any;
            let category: EnglishAdjectiveCategory = this._englishAdjectiveCategories[i];
            adjIndices = ogmaCat.getAdjectiveIndices() as Array<number>;

            innerCount = adjIndices.length;

            for (j = 0; j < innerCount; j++) {
                category.addAdjective(this._englishAdjectives[adjIndices[j]]);
                this._englishAdjectives[adjIndices[j]].category = category;
            }
        }

        outerCount = this._englishAdjectives.length;

        for (i = 0; i < outerCount; i++) {
            ogmaAdj = this._ogmaAdjectives[i] as any;
            adj = this._englishAdjectives[i];
            let conflictIndices: Array<number> = ogmaAdj.getConflictIndices() as Array<number>;

            innerCount = conflictIndices.length;

            console.log(ogmaAdj.getLexeme() + " has conflicts:");

            for (j = 0; j < innerCount; j++) {
                console.log("  " + this._englishAdjectives[conflictIndices[j]].getLexeme());
                adj.addConflict(this._englishAdjectives[conflictIndices[j]]);
            }
        }

        outerCount = this._englishNouns.length;

        for (i = 0; i < outerCount; i++) {
            let ogmaNoun: any = this._ogmaNouns[i] as any;
            let noun: EnglishNoun = this._englishNouns[i];
            adjIndices = ogmaNoun.getAdjectiveIndices() as Array<number>;

            innerCount = adjIndices.length;

            for (j = 0; j < innerCount; j++) {
                adj = this._englishAdjectives[adjIndices[j]];
                noun.addAppropriateAdjective(adj);
                adj.addNoun(noun);
            }
        }
    }

    buildModel(): boolean {
        this.buildCategories();
        this.buildAdjectives();
        this.buildNouns();

        this.link();

        return true;
    }
}
