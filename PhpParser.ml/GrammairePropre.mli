type token =
  | GCROCHET
  | T_EXIT
  | T_IF
  | LBRACKET
  | RBRACKET
  | DCROCHET
  | T_LNUMBER
  | T_DNUMBER
  | T_STRING
  | T_STRING_VARNAME
  | T_VARIABLE
  | T_NUM_STRING
  | T_INLINE_HTML
  | T_CHARACTER
  | T_BAD_CHARACTER
  | T_ENCAPSED_AND_WHITESPACE
  | T_CONSTANT_ENCAPSED_STRING
  | T_ECHO
  | T_DO
  | T_WHILE
  | T_ENDWHILE
  | T_FOR
  | T_ENDFOR
  | T_FOREACH
  | T_ENDFOREACH
  | T_DECLARE
  | T_ENDDECLARE
  | T_AS
  | T_SWITCH
  | T_ENDSWITCH
  | T_CASE
  | T_DEFAULT
  | T_BREAK
  | T_CONTINUE
  | T_GOTO
  | T_FUNCTION
  | T_CONST
  | T_RETURN
  | T_TRY
  | T_CATCH
  | T_THROW
  | T_USE
  | T_GLOBAL
  | T_VAR
  | T_UNSET
  | T_ISSET
  | T_EMPTY
  | T_HALT_COMPILER
  | T_CLASS
  | T_INTERFACE
  | T_EXTENDS
  | T_IMPLEMENTS
  | T_OBJECT_OPERATOR
  | T_DOUBLE_ARROW
  | T_LIST
  | T_ARRAY
  | T_CLASS_C
  | T_METHOD_C
  | T_FUNC_C
  | T_LINE
  | T_FILE
  | T_COMMENT
  | T_DOC_COMMENT
  | T_OPEN_TAG
  | T_OPEN_TAG_WITH_ECHO
  | T_CLOSE_TAG
  | T_WHITESPACE
  | T_START_HEREDOC
  | T_END_HEREDOC
  | T_DOLLAR_OPEN_CURLY_BRACES
  | T_CURLY_OPEN
  | T_PAAMAYIM_NEKUDOTAYIM
  | T_NAMESPACE
  | T_NS_C
  | T_DIR
  | T_NS_SEPARATOR

val start :
  (Lexing.lexbuf  -> token) -> Lexing.lexbuf -> Syntax.php
