package com.cornier.flashmath.Data

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Card: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var card_recto = ""
    var card_verso = ""
}