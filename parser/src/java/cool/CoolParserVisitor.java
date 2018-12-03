// Generated from CoolParser.g4 by ANTLR 4.5
package cool;

	import cool.AST;
	import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CoolParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CoolParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CoolParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(CoolParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#class_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_list(CoolParser.Class_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#class_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_(CoolParser.Class_Context ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#classMEMBER}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassMEMBER(CoolParser.ClassMEMBERContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#member}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMember(CoolParser.MemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#attr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttr(CoolParser.AttrContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethod(CoolParser.MethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_list(CoolParser.Arg_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(CoolParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#expression_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_list(CoolParser.Expression_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#block_expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_expr_list(CoolParser.Block_expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#branch_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBranch_list(CoolParser.Branch_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#branch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBranch(CoolParser.BranchContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#let_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLet_list(CoolParser.Let_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(CoolParser.ExpressionContext ctx);
}