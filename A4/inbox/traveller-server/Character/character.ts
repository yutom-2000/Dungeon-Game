interface ICharacter {
    name: string
}

class Character implements ICharacter {
    name: string
    constructor(name: string) {
        this.name = name
    }
}

export {
    Character,
    ICharacter
}