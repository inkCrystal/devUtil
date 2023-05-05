const OP_IN =  'IN' ;
const OP_NOT_IN =  'NOT_IN' ;
const OP_LIKE =  'LIKE' ;
const OP_NOT_LIKE =  'NOT_LIKE' ;
const OP_EQUAL =  'EQUAL' ;
const OP_NOT_EQUAL =  'NOT_EQUAL' ;
const OP_LESS_THAN =  'LESS_THAN' ;
const OP_LESS_THAN_OR_EQUAL =  'LESS_THAN_OR_EQUAL' ;
const OP_GREATER_THAN =  'GREATER_THAN' ;
const OP_GREATER_THAN_OR_EQUAL =  'GREATER_THAN_OR_EQUAL' ;
const OP_IS_NULL =  'IS_NULL' ;
const OP_IS_NOT_NULL =  'IS_NOT_NULL' ;
const OP_NOT_BETWEEN_AND =  'NOT_BETWEEN_AND' ;
const OP_BETWEEN_AND =  'BETWEEN_AND' ;
const OP_AND =  'AND' ;
const OP_OR =  'OR' ;

/**
 * 检查是否是 合法的操作符号
 * @param op
 * @returns {boolean}
 */
function checkOpEnum(op){
    switch (op){
        case OP_EQUAL : return  true;
        case OP_IN : return true;
        case OP_NOT_IN : return true;
        case OP_LESS_THAN : return true;
        case OP_LESS_THAN_OR_EQUAL : return true;
        case OP_GREATER_THAN : return true;
        case OP_GREATER_THAN_OR_EQUAL : return true;
        case OP_IS_NULL : return true;
        case OP_IS_NOT_NULL : return true;
        case OP_NOT_BETWEEN_AND : return true;
        case OP_BETWEEN_AND : return true;
        case OP_LIKE : return true;
        case OP_NOT_LIKE : return true;
        case OP_NOT_EQUAL : return true;
    }
    return false;
}

/**
 * 操作 针对参数 数量 的最小要求
 * @param op
 * @returns {number}
 */
function minParamLength(op){
    if(checkOpEnum(op)){
        switch (op){
            case OP_EQUAL : return  1;
            case OP_IN : return 1;
            case OP_NOT_IN : return 1;
            case OP_LESS_THAN : return 1;
            case OP_LESS_THAN_OR_EQUAL : return 1;
            case OP_GREATER_THAN : return 1;
            case OP_GREATER_THAN_OR_EQUAL : return 1;
            case OP_IS_NULL : return 0;
            case OP_IS_NOT_NULL : return 0;
            case OP_NOT_BETWEEN_AND : return 2;
            case OP_BETWEEN_AND : return 2;
            case OP_LIKE : return 1;
            case OP_NOT_LIKE : return 1;
        }

    }
    return -1;
}

/**
 * 操作 针对参数 数量的最大要求
 * @param op
 * @returns {number}
 */
function maxParamLength(op){
    if(checkOpEnum(op)){
        switch (op){
            case OP_EQUAL : return  1;
            case OP_IN : return 9999;
            case OP_NOT_IN : return 9999;
            case OP_LESS_THAN : return 1;
            case OP_LESS_THAN_OR_EQUAL : return 1;
            case OP_GREATER_THAN : return 1;
            case OP_GREATER_THAN_OR_EQUAL : return 1;
            case OP_IS_NULL : return 0;
            case OP_IS_NOT_NULL : return 0;
            case OP_NOT_BETWEEN_AND : return 2;
            case OP_BETWEEN_AND : return 2;
            case OP_LIKE : return 1;
            case OP_NOT_LIKE : return 1;
        }

    }
    return -1;
}
/***********************CONST*******************************/
const PARAM_TYPE_VALUE = 'v';
const PARAM_TYPE_KEY = 'k';
/*****************DEF********************************/
function Node(k , op , isKeyType ){
    this.k = k;
    // 操作符号
    this.op = op;
    // 里面 存放新的filter 链路
    // 是否 参数是 key ，而不是
    this.keyParamType = isKeyType;
    this.params = [];

    this.nextNode = null;
    this.preNode = null;
    this.outerNode = null;
    this.innerNode = null;

}
function selectFilter(){
    this.body   = null;
    this.current = null;
    this.preInner = false;

    this._appendNode = function (node){
        if (this.body == null) {
            this.body = node;
        }else{
            if(this.preInner){
                this.preInner = false;
                node.outerNode = this.current;
                this.current.innerNode = node;
                this.current = node;
            }else{
                node.preNode = this.current;
                this.current.nextNode = node;
            }


        }
        this.current = node;

    }

    this.eq = function (k , v){
        let note = new Node(k , OP_EQUAL , false);
        note.params.push(v);
        this._appendNode(note);
        return this;
    }
    this.eqKey = function (k , targetKey){
        let note = new Node(k , OP_EQUAL , true);
        note.params.push(targetKey);
        this._appendNode(note);
        return this;
    }

    this.notEq = function (k , v){
        let note = new Node(k , OP_NOT_EQUAL , false);
        note.params.push(v);
        this._appendNode(note);
        return this;
    }

    this.notEqKey = function (k , targetKey){
        let note = new Node(k , OP_NOT_EQUAL , true);
        note.params.push(targetKey);
        this._appendNode(note);
        return this;
    }


    this.in = function (k , ...values){
        let note = new Node(k , OP_IN , false);
        if (values.length > 1) {
            for (let i = 0; i < values.length; i++) {
                console.log("push in value : " + values[i]);
                note.params.push(values[i]);
            }
        }
        this._appendNode(note);
        return this;
    }

    this.notIn = function (k , values){
        let note = new Node(k , OP_NOT_IN , false);
        if (values.length > 1) {
            for (let i = 0; i < values.length; i++) {
                note.params.push(values[i]);
            }
        }
        this._appendNode(note);
        return this;
    }

    this.like = function (k , v){
        let note = new Node(k , OP_LIKE , false);
        note.params.push(v);
        this._appendNode(note);
        return this;
    }

    this.notLike = function (k , v){
        let note = new Node(k , OP_NOT_LIKE , false);
        note.params.push(v);
        this._appendNode(note);
        return this;
    }

    this.lessThan = function (k , v){
        let note = new Node(k , OP_LESS_THAN , false);
        note.params.push(v);
        this._appendNode(note);
        return this;
    }

    this.lessThanOrEqual = function (k , v){
        let note = new Node(k , OP_LESS_THAN_OR_EQUAL , false);
        note.params.push(v);
        this._appendNode(note);
        return this;
    }

    this.lessThanKey = function (k , targetKey){
        let note = new Node(k , OP_LESS_THAN , true);
        note.params.push(targetKey);
        this._appendNode(note);
        return this;
    }

    this.lessThanOrEqualKey = function (k , targetKey){
        let note = new Node(k , OP_LESS_THAN_OR_EQUAL , true);
        note.params.push(targetKey);
        this._appendNode(note);
        return this;
    }

    this.greaterThan = function (k , v){
        let note = new Node(k , OP_GREATER_THAN , false);
        note.params.push(v);
        this._appendNode(note);
        return this;
    }

    this.greaterThanOrEqual = function (k , v){
        let note = new Node(k , OP_GREATER_THAN_OR_EQUAL , false);
        note.params.push(v);
        this._appendNode(note);
        return this;
    }

    this.graterThanKey = function (k , targetKey){
        let note = new Node(k , OP_GREATER_THAN , true);
        note.params.push(targetKey);
        this._appendNode(note);
        return this ;
    }

    this.graterThanOrEqualKey = function (k , targetKey){
        let note = new Node(k , OP_GREATER_THAN_OR_EQUAL , true);
        note.params.push(targetKey);
        this._appendNode(note);
        return this;
    }

    this.between = function (k , v1 , v2){
        let note = new Node(k , OP_BETWEEN_AND , false);
        note.params.push(v1);
        note.params.push(v2);
        this._appendNode(note);
        return this;
    }

    this.notBetween = function (k , v1 , v2){
        let note = new Node(k , OP_NOT_BETWEEN_AND , false);
        note.params.push(v1);
        note.params.push(v2);
        this._appendNode(note);
        return this;
    }

    this.beginOR = function (){
        if(this.current == null) {
            throw new Error('尚未有任何条件，不能OR');
        }
        if (this.current.innerNode == null) {
            this.preInner =true;
        }
        return this;
    }

    this.breakOR = function () {
        let nodeFirst = this.current;
        while (nodeFirst.preNode !=null){
            nodeFirst = nodeFirst.preNode;
        }
        if (nodeFirst.outerNode != null) {
            this.current = nodeFirst.outerNode;
        } else {
            throw new Error('未发现有开始的OR逻辑，不能BREAK OR');
        }
        return this;
    }



    this.toJSON = function (){
        let json = {body:[]};
        nodeAppend(json, this.body);
        return json;
    }

    this.toJSONString = function (){
        return JSON.stringify(this.toJSON());
    }






}

function nodeAppend(json , node){
    let nodeJson = simpleNodeToJson(node);
    if(node == null){
        return ;
    }
    if(node.innerNode != null){
        let inNode = node.innerNode;
        if(nodeJson.body == null){
            nodeJson.body = [];
        }
        nodeAppend(nodeJson, inNode);
    }
    if(json.body == null){
        json.body = [];
    }
    json.body.push(nodeJson);
    if(node.nextNode!=null){
        nodeAppend(json, node.nextNode);
    }

}



function simpleNodeToJson(node){
    if( "Node" == typeof  node){
        throw new Error('参数类型错误，必须是Node');
    }
    let json = {
        op: node.op,
        k: node.key,
        vType: node.keyParamType?'k':'v',
        v: node.params
    };
    // if(node.params.length > 0){
    //     console.log(node.params);
    //     for (let i = 0; i < node.params.length; i++) {
    //         json.v.push(node.params[i]);
    //     }
    // }
    return json ;
}