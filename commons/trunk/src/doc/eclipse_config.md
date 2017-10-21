## Tab & Spaces Policy

### Java Editor

+ Click Window » Preferences
+ Expand Java » Code Style
+ Click Formatter
+ Click the Edit button
+ Click the Indentation tab
+ Under General Settings, set Tab policy to: Spaces only
+ Click OK ad nauseam to apply the changes.
[Note: If necessary save profile with a new name as the default profile cannot be overwritten.]

### Default Text Editor

+ Before version 3.6:

```
Window->Preferences->Editors->Text Editors->Insert spaces for tabs
```

+ Version 3.6 and later:

- Click Window » Preferences
- Expand General » Editors
- Click Text Editors
- Check Insert spaces for tabs
- Click OK ad nauseam to apply the changes.
Note that the default text editor is used as the basis for many non-Java editors in Eclipse. It's astonishing that this setting wasn't available until 3.3.

### C / C++

+ Click Window » Preferences
+ Expand C/C++ » Code Style
+ Click Formatter
+ Click the New button to create a new profile, then OK to continue
+ Click the Indentation tab
+ Under General Settings, set Tab policy to: Spaces only
+ Click OK ad nauseam to apply the changes.

### HTML

+ Click Window » Preferences
+ Expand Web » HTML Files
+ Click Editor
+ Under Formatting, select the Indent using spaces radio button
+ Click OK to apply the changes.

### CSS

Follow the same instructions for HTML, but select CSS Files instead of HTML Files.

### JSP

By default, JSP files follow the formatting preferences for HTML Files.

### XML

+ XML files spacing is configured in Preferences.

- Click Window » Preferences
-  XML » XML Files
- Click Editor
- Select Indent using spaces
- You can specify the Indentation size if needed: number of spaces to indent.

## General \ Appearance \ Label Decorations

- 推荐：在“Package Explorer”视图中工程名的后面可以同时显示maven工程的版本号，是个非常有用的功能。
  + 在“Availiable label decorations”列表中找到“Maven Version Decorator”，将其选中。

## General \ Workspace

- 将工作空间文本文件默认编码设置为UTF-8。
  + 将位于左下角的“Text file encoding”设置为“UTF-8”。

## Java \ Code Style \ Code Templates

- 将以下内容复制到codetemplate.xml中：
```
<templates><template autoinsert="false" context="filecomment_context" deleted="false" description="Comment for created Java files" enabled="true" id="org.eclipse.jdt.ui.text.codetemplates.filecomment" name="filecomment">/**
 * 版权所有(C)，XX股份有限公司，${year}，所有权利保留。
 * 
 * 项目名：	${project_name}
 * 文件名：	${file_name}
 * 模块说明：	
 * 修改历史：
 * ${date} - ${user} - 创建。
 */</template></templates>
```
- 在“Preferences”对话框中，点击“Import...”按钮，导入刚才生成的文件。

## Java \ Code Style \ Formatter

- 将以下内容复制到format_template.xml中:
```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<profiles version="12">
<profile kind="CodeFormatterProfile" name="code format" version="12">
<setting id="org.eclipse.jdt.core.formatter.comment.insert_new_line_before_root_tags" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.disabling_tag" value="@formatter:off"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_annotation" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_type_parameters" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_type_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_type_arguments" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_anonymous_type_declaration" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_colon_in_case" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_brace_in_array_initializer" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.new_lines_at_block_boundaries" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_in_empty_annotation_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_before_closing_brace_in_array_initializer" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_annotation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_before_field" value="0"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_while" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.use_on_off_tags" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_between_empty_parens_in_annotation_type_member_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_before_else_in_if_statement" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_prefix_operator" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.keep_else_statement_on_same_line" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_ellipsis" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.insert_new_line_for_parameter" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_annotation_type_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.indent_breaks_compare_to_cases" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_at_in_annotation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_multiple_fields" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_expressions_in_array_initializer" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_conditional_expression" value="80"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_for" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_binary_operator" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_question_in_wildcard" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_array_initializer" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_between_empty_parens_in_enum_constant" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_before_finally_in_try_statement" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_after_annotation_on_local_variable" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_before_catch_in_try_statement" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_while" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_after_package" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_type_parameters" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.continuation_indentation" value="2"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_postfix_operator" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_arguments_in_method_invocation" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_angle_bracket_in_type_arguments" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_superinterfaces" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_before_new_chunk" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_binary_operator" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_before_package" value="0"/>
<setting id="org.eclipse.jdt.core.compiler.source" value="1.7"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_enum_constant_arguments" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_constructor_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.format_line_comments" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_closing_angle_bracket_in_type_arguments" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_enum_declarations" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.join_wrapped_lines" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_block" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_arguments_in_explicit_constructor_call" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_method_invocation_arguments" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_before_member_type" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.align_type_members_on_columns" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_enum_constant" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_for" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_method_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_selector_in_method_invocation" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_switch" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_unary_operator" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_colon_in_case" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.indent_parameter_description" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_method_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_switch" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_enum_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_angle_bracket_in_type_parameters" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.clear_blank_lines_in_block_comment" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_in_empty_type_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.lineSplit" value="100"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_if" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_between_brackets_in_array_type_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_parenthesized_expression" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_explicitconstructorcall_arguments" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_constructor_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_before_first_class_body_declaration" value="0"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_after_annotation_on_method" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.indentation.size" value="4"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_between_empty_parens_in_method_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.enabling_tag" value="@formatter:on"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_enum_constant" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_superclass_in_type_declaration" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_assignment" value="0"/>
<setting id="org.eclipse.jdt.core.compiler.problem.assertIdentifier" value="error"/>
<setting id="org.eclipse.jdt.core.formatter.tabulation.char" value="space"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_constructor_declaration_parameters" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_semicolon_in_try_resources" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_prefix_operator" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.indent_statements_compare_to_body" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_before_method" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.wrap_outer_expressions_when_nested" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.format_guardian_clause_on_one_line" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_colon_in_for" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_cast" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_parameters_in_constructor_declaration" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_colon_in_labeled_statement" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_annotation_type_declaration" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_in_empty_method_body" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_method_declaration" value="0"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_method_invocation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_try" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_bracket_in_array_allocation_expression" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_enum_constant" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_annotation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_at_in_annotation_type_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_method_declaration_throws" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_if" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_switch" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_method_declaration_throws" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_parenthesized_expression_in_return" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_annotation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_question_in_conditional" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_question_in_wildcard" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_try" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_bracket_in_array_allocation_expression" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.preserve_white_space_between_code_and_line_comments" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_parenthesized_expression_in_throw" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_type_arguments" value="do not insert"/>
<setting id="org.eclipse.jdt.core.compiler.problem.enumIdentifier" value="error"/>
<setting id="org.eclipse.jdt.core.formatter.indent_switchstatements_compare_to_switch" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_ellipsis" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_block" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_for_inits" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_method_declaration" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.compact_else_if" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.wrap_before_or_operator_multicatch" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_array_initializer" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_for_increments" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.format_line_comment_starting_on_first_column" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_bracket_in_array_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_after_annotation_on_field" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_enum_constant" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.comment.indent_root_tags" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_enum_declarations" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_union_type_in_multicatch" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_explicitconstructorcall_arguments" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_switch" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_method_declaration_parameters" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_superinterfaces" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_allocation_expression" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.tabulation.size" value="2"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_bracket_in_array_type_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_after_opening_brace_in_array_initializer" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_closing_brace_in_block" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_bracket_in_array_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_in_empty_enum_constant" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_angle_bracket_in_type_arguments" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_constructor_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_constructor_declaration_throws" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_if" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.clear_blank_lines_in_javadoc_comment" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_throws_clause_in_constructor_declaration" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_assignment_operator" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_assignment_operator" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.indent_empty_lines" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_synchronized" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_closing_paren_in_cast" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_method_declaration_parameters" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_block_in_case" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.number_of_empty_lines_to_preserve" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_method_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_catch" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_constructor_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_method_invocation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_bracket_in_array_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_arguments_in_qualified_allocation_expression" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_and_in_type_parameter" value="insert"/>
<setting id="org.eclipse.jdt.core.compiler.compliance" value="1.7"/>
<setting id="org.eclipse.jdt.core.formatter.continuation_indentation_for_array_initializer" value="2"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_between_empty_brackets_in_array_allocation_expression" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_at_in_annotation_type_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_arguments_in_allocation_expression" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_cast" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_unary_operator" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_angle_bracket_in_parameterized_type_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_anonymous_type_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.keep_empty_array_initializer_on_one_line" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_in_empty_enum_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.keep_imple_if_on_one_line" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_constructor_declaration_parameters" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_closing_angle_bracket_in_type_parameters" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_at_end_of_file_if_missing" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_colon_in_for" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_colon_in_labeled_statement" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_parameterized_type_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_superinterfaces_in_type_declaration" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_binary_expression" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_enum_declaration" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_after_annotation_on_type" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_while" value="do not insert"/>
<setting id="org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode" value="enabled"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_try" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.put_empty_statement_on_new_line" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_after_label" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_after_annotation_on_parameter" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_angle_bracket_in_type_parameters" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_between_empty_parens_in_method_invocation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_before_while_in_do_statement" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_arguments_in_enum_constant" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.comment.format_javadoc_comments" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.comment.line_length" value="80"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_after_annotation_on_package" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_between_import_groups" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_enum_constant_arguments" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_semicolon" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_constructor_declaration" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.number_of_blank_lines_at_beginning_of_method_body" value="0"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_colon_in_conditional" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.indent_body_declarations_compare_to_type_header" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_annotation_type_member_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.wrap_before_binary_operator" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.indent_body_declarations_compare_to_enum_declaration_header" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_between_type_declarations" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_synchronized" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.indent_statements_compare_to_block" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_superinterfaces_in_enum_declaration" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.join_lines_in_comments" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_question_in_conditional" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_multiple_field_declarations" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_compact_if" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_for_inits" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.indent_switchstatements_compare_to_cases" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_array_initializer" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_colon_in_default" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_and_in_type_parameter" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_between_empty_parens_in_constructor_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_before_imports" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_colon_in_assert" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.format_html" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_throws_clause_in_method_declaration" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_angle_bracket_in_type_parameters" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_bracket_in_array_allocation_expression" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_in_empty_anonymous_type_declaration" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_colon_in_conditional" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_angle_bracket_in_parameterized_type_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_for" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_postfix_operator" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.format_source_code" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_synchronized" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_allocation_expression" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_constructor_declaration_throws" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_parameters_in_method_declaration" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_brace_in_array_initializer" value="insert"/>
<setting id="org.eclipse.jdt.core.compiler.codegen.targetPlatform" value="1.7"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_resources_in_try" value="80"/>
<setting id="org.eclipse.jdt.core.formatter.use_tabs_only_for_leading_indentations" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_arguments_in_annotation" value="16"/>
<setting id="org.eclipse.jdt.core.formatter.comment.format_header" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.comment.format_block_comments" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_enum_constant" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.alignment_for_enum_constants" value="0"/>
<setting id="org.eclipse.jdt.core.formatter.insert_new_line_in_empty_block" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.indent_body_declarations_compare_to_annotation_declaration_header" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_parenthesized_expression" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_parenthesized_expression" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_catch" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_multiple_local_declarations" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_switch" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_comma_in_for_increments" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_method_invocation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_colon_in_assert" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.brace_position_for_type_declaration" value="end_of_line"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_brace_in_array_initializer" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_between_empty_braces_in_array_initializer" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_opening_paren_in_method_declaration" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_semicolon_in_for" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_paren_in_catch" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_angle_bracket_in_parameterized_type_reference" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_multiple_field_declarations" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_closing_paren_in_annotation" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_parameterized_type_reference" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_method_invocation_arguments" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.comment.new_lines_at_javadoc_boundaries" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.blank_lines_after_imports" value="1"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_comma_in_multiple_local_declarations" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.indent_body_declarations_compare_to_enum_constant_header" value="true"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_after_semicolon_in_for" value="insert"/>
<setting id="org.eclipse.jdt.core.formatter.never_indent_line_comments_on_first_column" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_semicolon_in_try_resources" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.insert_space_before_opening_angle_bracket_in_type_arguments" value="do not insert"/>
<setting id="org.eclipse.jdt.core.formatter.never_indent_block_comments_on_first_column" value="false"/>
<setting id="org.eclipse.jdt.core.formatter.keep_then_statement_on_same_line" value="false"/>
</profile>
</profiles>
```

- 在“Preferences”对话框中，点击“Import...”按钮，导入刚才创建的文件。
- “Active profile”确保选择“rumba format”，点击“Apply”按钮。

## Validation
- 点击"Disable All"按钮。

## Team \ Ignored Resources
- Team \ Ignored Resources
  + 点击“Add Pattern...”。
  + 在弹出对话框中输入“.*”，点击“OK”按钮。

## Java \ Compiler \ Errors/Warnings

#### Code Style

| Eclipse | IntelliJ IDEA | 值 |
| --- | --- | --- |
| Non-static access to a static member | Access static member via instance reference | **warning** |
| Indirect access to a static member | Static fields referenced via subclass<br>Static method referenced via subclass | **ignore** |
| Unqualified access to instance field | Instance field access not qualified with `this` | **ignore** |
| Access to a non-accessible member of an enclosing type | Accessing a non-public field of another object | **ignore** |
| Parameter assignment | Assignment to method parameter | **ignore** |
| Non-externalized strings |  | **ignore** |
| Undocumented empty block: **ignore** | Statement with empty body: **warning**<br><ul><li>Include statment bodies that are empty code blocks: **true**</li><li>Comments count as content: **true**</li></ul> | 各自设置|
| Resource not managed via try-with-resource (1.7 or higher) | AutoCloseable used without try-with-resources | **ignore** |
| Method with a constructor name |  | **warning** |
| Method can be static | Method may be `static`<br><ul><li>Only check `private` or `final` methods: **true**</li><li>Ignore empty methods: **false**</li><li>Ignore `default` methods: **true**</li><li>Quick fix replaces instance qualifiers with class references: **true**</li></ul> | **warning** |
| Method can potentially be static | 同上 | **ignore** | 

#### Potential programming problems

| Eclipse | IntelliJ IDEA | 值 |
| --- | --- | --- |
| Comparing identical values (`x == x`) |  | **warning** |
| Assignment has no effect (e.g. `x = x`) | Variable is assigned to itself | **warning** |
| Possible accidental boolean assignment (e.g. `if (a = b)`) | Assignment used as condition | **warning** |
| Boxing and unboxing conversions | Auto-boxing, Auto-unboxing | **ignore** |
| Using a char array in string concatenation |  | **warning** |
| Inexact type match for vararg arguments | Confusing argument to varargs method, Confusing primitive array argument to varargs method | **warning** |
| Empty statement | Unnecessary semicolon | **ignore** |
| Unused object allocation | Result of object allocation ignored | **warning** |
| Incomplete `switch` cases on enum | Enum `switch` statement that misses case | **ignore** |
| `switch` is missing `default` case | `switch` statement with no `default` branch | **ignore** |
| `switch` case fall-through | Fall-through in `switch` statement | **warning** |
| Hidden catch block | Overly broad `catch` block<br><ul><li>Only warn on RuntimeException, Exception, Error or Throwable: **false**</li><li>Ignore exception whitch hide others but are themsalves thrown: **false**</li></ul> | **warning** |
| `finally` does not complete normally | `finally` block which can not complete normally;<br>`return` inside `finally` block;<br>`throw` inside `finally` block; | **warning** |
| Dead code (e.g. `if (false)`) |  | **warning** |
| Resource leak |  | **warning** |
| Potential resource leak |  | **ignore** |
| Serializable class without serialVersionUID | Serializable class without serialVersionUID | **warning** |
| Missing synchronized modifier on inherited method |  Unsynchronized method overrides synchronized method | **ignore** |
| Class overrides `equals()` but not `hashCode()` | `equals()` and `hashCode()` not paired | **ignore** |

#### Name shadowing and conflicts

| Eclipse | IntelliJ IDEA | 值 |
| --- | --- | --- |
| Field declaration hides another field or variable | Field name hides field in superclass | **ignore** |
| Local variable declaration hides another field or variable | Local variable hides field | **ignore** |
| Type parameter hides another type | Type parameter hides visible type | **warning** |
| Method does not override package visible method | Method overrides package-local method of superclass located in other package | **warning** |
| Interface method conflicts with protected 'Object' method | Interface method clashes with method in `java.lang.Object` | **warning** |

#### Deprecated and restricted API

| Eclipse | IntelliJ IDEA | 值 |
| --- | --- | --- |
| Deprecated API | Deprecated API usage<br><ul><li>Igonre inside deprecated members: **false**</li><li>Ignore inside non-static imports: **true**</li><li>Ignore overrides of deprecated abstract methods from non-deprecated supers: **true**</li><li>Ignore members of deprecated classes: **true**</li></ul> | **warning** |
| Signal use of deprecated API inside deprecated code |  | **false** |
| Signal overriding or implementing deprecated method |  | **false** |
| Forbidden reference (access rules) |  | **error** |
| Discouraged reference (access rules) |  | **warning** |

#### Unnecessary code

| Eclipse | IntelliJ IDEA | 值 |
| --- | --- | --- |
| Value of local variable is not used | Unused declaration | **warning** |
| Value of method parameter is not used | Unused method parameters | **ignore** |
| Unused type parameter |  | **ignore** |
| Ignore parameters documented with '@param' tag | Unused method parameters(Configure annotations) | **true** |
| Value of exception parameter is not used | Unused `catch` parameter | **ignore** |
| Unused import | Unused import | **warning** |
| Unused private member | Unused declaration | **warning** |
| Unnecessary 'else' statement | Confusing `else` branch | **ignore** |
| Unnecessary cast or `instanceof` operation | Redundant type cast | **warning** |
| Unnecessary declaration of thrown exception | Redundant throws declaration | **ignore** |
| Unused 'break' or 'continue' label | Unused label | **warning** |
| Redundant super interface | Redundant interface declaration<br><ul><li>Ignore `java.io.Serializable`: **false**</li><li>Ignore `java.lang.Cloneable`: **false**</li></ul> | **warning** |

#### Generic types

| Eclipse | IntelliJ IDEA | 值 |
| --- | --- | --- |
| Unchecked generic type operation |  | **ignore** |
| Usage of a raw type | Raw use of parameterized class | **ignore** |
| Generic type parameter declared with a final type bound |  | **warning** |
| Redundant type arguments(1.7 or higher) Ignore unavoidable generic type problems due to raw APIs |  | **ignore** |
| Ignore unavoidable generic type problems due to raw APIs |  | **false** |


#### Annotations

| Eclipse | IntelliJ IDEA | 值 |
| --- | --- | --- |
| Missing `@Override` annotation | Missing `@Override` annotation  | **ignore** |
| include implementation of interface methods (1.6 or higher) |  | **true** |
| Missing `@Deprecated` annotation | Missing `@Deprecated` annotation | **ignore** |
| Annotation is used as super interface | Class extends annotation interface | **warning** |
| Unhandled token in '@SuppressWarnings' |  | **warning** |
| Enable '@SuppressWarnings' annotations |  | **true** |
| Unused `@SuppressWarnings` token |  | **warning** |
| Suppress optional errors with `@SuppressWarnings` |  | **false** |

#### Null analysis

| Eclipse | IntelliJ IDEA | 值 |
| --- | --- | --- |
| Null pointer access |  | **warning** |
| Potential null pointer access |  | **ignore** |
| Redundant null check |  | **warning** |
| include `assert` in null analysis |  | **off** |
| Enable annotation-based null analysis |  | **off** |



