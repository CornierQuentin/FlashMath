package com.cornier.flashmath.Data

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

abstract class Pack() : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var pack_name = ""
    var pack_description = ""

    var number_of_card = 0
    var cards = emptyArray<Card>()
    constructor(packName: String = "No name") : this() {
        pack_name = packName
    }
}