#include "expression.h"

#include <algorithm>
#include <iostream>
#include <cmath>

#define DEFINE_OPERATOR(op)						\
	Expression& Expression::operator op## =	(const Expression &e)	\
	{											\
		addFunction(findFunction(#op, Function<double>::Type::INFIX), e); \
		return *this;							\
	}											\

#define DEFINE_OPERATORV(op)					\
	Expression Expression::operator op(const Expression &e) const	\
	{											\
		Expression	res(*this);					\
		res op## = e;							\
		return res;								\
	}

using std::cout;
using std::endl;

namespace {
    const Functions<double> operators = {
            Function<double>("+", 10, [](const Args<double> &a){return a[0] + a[1];}, true),
            Function<double>("-", 10, [](const Args<double> &a){return a[0] - a[1];}, false),
            Function<double>("*", 20, [](const Args<double> &a){return a[0] * a[1];}, true),
            Function<double>("/", 20, [](const Args<double> &a){return a[0] / a[1];}, false),
            Function<double>("^", 30, [](const Args<double> &a){return std::pow(a[0], a[1]);}, false),
            Function<double>("⁻¹", 30, [](const Args<double> &a){return 1 / a[0];}, Function<double>::Type::POSTFIX),
            Function<double>("-", 40, [](const Args<double> &a){return -a[0];}, Function<double>::Type::PREFIX)};
    const Functions<double> functions = {
            Function<double>("ln", [](const Args<double> &a){return std::log(a[0]);}),
            Function<double>("sqrt", [](const Args<double> &a){return std::sqrt(a[0]);}),
            Function<double>("abs", [](const Args<double> &a){return std::abs(a[0]);}),
            Function<double>("ceil", [](const Args<double> &a){return ceil(a[0]);}),
            Function<double>("floor", [](const Args<double> &a){return floor(a[0]);}),
            Function<double>("max", [](const Args<double> &a){return std::max(a[0], a[1]);}, 2),
            Function<double>("min", [](const Args<double> &a){return std::min(a[0], a[1]);}, 2),

            Function<double>("sin", [](const Args<double> &a){return sin(a[0]);}),
            Function<double>("cos", [](const Args<double> &a){return cos(a[0]);}),
            Function<double>("tan", [](const Args<double> &a){return tan(a[0]);}),
            Function<double>("ctg", [](const Args<double> &a){return 1.0 / tan(a[0]);}),
            Function<double>("asin", [](const Args<double> &a){return asin(a[0]);}),
            Function<double>("acos", [](const Args<double> &a){return acos(a[0]);}),
            Function<double>("atan", [](const Args<double> &a){return atan(a[0]);}),
            Function<double>("atan2", [](const Args<double> &a){return atan2(a[0], a[1]);}, 2),

            Function<double>("cosh", [](const Args<double> &a){return cosh(a[0]);}),
            Function<double>("sinh", [](const Args<double> &a){return sinh(a[0]);}),
            Function<double>("tanh", [](const Args<double> &a){return tanh(a[0]);}),
            Function<double>("ctgh", [](const Args<double> &a){return 1.0 / (a[0]);}),
            Function<double>("acosh", [](const Args<double> &a){return acosh(a[0]);}),
            Function<double>("asinh", [](const Args<double> &a){return asinh(a[0]);}),
            Function<double>("atanh", [](const Args<double> &a){return atanh(a[0]);}),
            Function<double>("actgh", [](const Args<double> &a){return atanh(1.0 /a[0]);})};
}

Expression::Expression(const std::string &s) :
        m_root(nullptr)
{
    ExpressionParserSettings <double> set(operators, functions, m_varnames);
    set.regex_whitespace = std::regex("^[[:space:]]+");
    set.regex_constant = std::regex("^[[:digit:]]+\\.?[[:digit:]]*");
    set.regex_parenthesis_begin = std::regex("^\\(");
    set.regex_parenthesis_end = std::regex("^\\)");
    set.regex_variable = std::regex("^(e|π)");
    set.regex_function_begin = std::regex("^[[:alpha:]][[:alnum:]]*[[:space:]]*\\(");
    set.regex_function_end = std::regex("^\\)");
    set.regex_func_args_separator = std::regex("^,");
    ExpressionParser <double> p(set, s);
    m_root = p.parse();
    if(m_root) {
        //cout << "You entered: " << endl;
        //m_root->print();
        //cout << endl;
        for(const auto &s : m_varnames) {
            m_variables[s] = 0.0;
        }

        //cout << "expression: " << endl;
        //for(auto it = m_root->begin(); it != m_root->end(); ++it) {
        //    it->print();
        //    cout << ", ";
        //}
    }

    m_variables["e"] = 2.7182818284;
    m_variables["π"] = acos(-1.0);
}

Expression::Expression(const Expression &e) :
        m_root(nullptr),
        m_variables(e.m_variables)
{
    m_root = new Cell <double>(*e.m_root);
}

Expression& Expression::operator=(const Expression &e)
{
    if(this != &e) {
        if(m_root != nullptr) {
            delete m_root;
        }
        m_root = new Cell <double>(*e.m_root);
        m_variables = e.m_variables;
        m_varnames = e.m_varnames;
    }
    return *this;
}

bool Expression::operator==(const Expression &e) const
{
    return (m_root == e.m_root) || ((m_root != nullptr) && (e.m_root != nullptr) && (*m_root == *e.m_root));
}

bool Expression::operator!=(const Expression &e) const
{
    return !(*this == e);
}

DEFINE_OPERATOR(+);
DEFINE_OPERATOR(-);
DEFINE_OPERATOR(*);
DEFINE_OPERATOR(/);

DEFINE_OPERATORV(+);
DEFINE_OPERATORV(-);
DEFINE_OPERATORV(*);
DEFINE_OPERATORV(/);

bool Expression::isSubExpression(const Expression &e) const
{
    std::vector <Cell <double>*> curcell;
    curcell.push_back(e.m_root);
    while(curcell[curcell.size() - 1]->type == Cell <double>::Type::FUNCTION) {
        curcell.push_back(curcell[curcell.size() - 1]->func.args[0]);
    }
    bool tmp;
    return m_root->isSubExpression(curcell, tmp);
}

std::map <std::string, double>& Expression::variables()
{
    return m_variables;
}

double Expression::getVar(size_t id) const
{
    if(id >= m_varnames.size()) {
        throw ExpressionException("Index out of range");
    }
    return m_variables.at(m_varnames[id]);
}

double Expression::getVar(const std::string &name) const
{
    auto it = m_variables.find(name);
    if(it == m_variables.end()) {
        throw ExpressionException("Undefined variable: " + name);
    }
    return m_variables.at(name);
}

void Expression::setVar(size_t id, double val)
{
    m_variables[m_varnames[id]] = val;
}

void Expression::setVar(const std::string &name, double val)
{
    auto it = m_variables.find(name);
    if(it == m_variables.end()) {
        throw ExpressionException("Undefined variable: " + name);
    }
    it->second = val;
}

Functions<double>::const_iterator Expression::findFunction(const std::string &name, Function<double>::Type type)
{
    auto res = operators.end();
    for(auto i = operators.begin(); i != operators.end(); ++i) {
        if((type == i->type) && (name == i->name)) {
            res = i;
        }
    }
    return res;
}

void Expression::addFunction(const Functions<double>::const_iterator &f, const Expression &e)
{
    Cell <double> *tmp = m_root;
    Cell <double> *arg2 = new Cell <double>(*e.m_root);
    m_root = new Cell <double>();
    m_root->type = Cell <double>::Type::FUNCTION;
    m_root->func.iter = f;
    m_root->func.args.push_back(tmp);
    m_root->func.args.push_back(arg2);
}

void Expression::print()
{
    m_root->print();
}


double Expression::eval()
{
    return m_root->eval(m_variables);
}
