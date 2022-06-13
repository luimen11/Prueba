grammar Gramatica;

options {caseInsensitive = true; }

INTR : 'int';
STRINGR : 'string';
IMPRIMIRR: 'imprimir';
SUBR : 'subroutine';
ENDR : 'end';

INT     : [0-9]* ;
IDEN    : [_a-zA-Z][a-zA-Z0-9_]* ;
STRING  : '"' (~["\r\n]+ | '""')* '"' ;
WS      : [ \t\r\n]+ -> skip ;

start       : linstrucciones
            ;

linstrucciones : instrucciones (instrucciones) *
               ;

instrucciones : block
              | declaration
              | print
              | subroutine
              | call
              ;

subroutine    : SUBR id1=IDEN '('  lexpr ')' 'implicit none' linstrucciones END SUBR id2=IDEN
                ;

call : 'call' IDEN '(' lexpr ')';

lexpr           : expr (',' expr)*
                ;


print         : IMPRIMIRR '(' expr ')' ';'
              ;

block   : '{' linstrucciones '}'
        ;

declaration : type IDEN '=' expr ';' ;

type    : INTR
        | STRINGR
        ;

expr    : left=expr op=('*'|'/') right=expr #opExpr
        | left=expr op=('+'|'-') right=expr #opExpr
        | '(' expr ')'                      #parenExpr
        | atom=INT                          #atomExpr
        | str=STRING                        #strExpr
        | id=IDEN                           #idExpr
        | IDEN '(' lexpr ')'                #funcExpr
        ;


