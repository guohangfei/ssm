package entity;

import java.util.ArrayList;
import java.util.List;

public class RightExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RightExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andRightIdIsNull() {
            addCriterion("RIGHT_ID is null");
            return (Criteria) this;
        }

        public Criteria andRightIdIsNotNull() {
            addCriterion("RIGHT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andRightIdEqualTo(String value) {
            addCriterion("RIGHT_ID =", value, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdNotEqualTo(String value) {
            addCriterion("RIGHT_ID <>", value, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdGreaterThan(String value) {
            addCriterion("RIGHT_ID >", value, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdGreaterThanOrEqualTo(String value) {
            addCriterion("RIGHT_ID >=", value, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdLessThan(String value) {
            addCriterion("RIGHT_ID <", value, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdLessThanOrEqualTo(String value) {
            addCriterion("RIGHT_ID <=", value, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdLike(String value) {
            addCriterion("RIGHT_ID like", value, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdNotLike(String value) {
            addCriterion("RIGHT_ID not like", value, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdIn(List<String> values) {
            addCriterion("RIGHT_ID in", values, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdNotIn(List<String> values) {
            addCriterion("RIGHT_ID not in", values, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdBetween(String value1, String value2) {
            addCriterion("RIGHT_ID between", value1, value2, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightIdNotBetween(String value1, String value2) {
            addCriterion("RIGHT_ID not between", value1, value2, "rightId");
            return (Criteria) this;
        }

        public Criteria andRightNameIsNull() {
            addCriterion("RIGHT_NAME is null");
            return (Criteria) this;
        }

        public Criteria andRightNameIsNotNull() {
            addCriterion("RIGHT_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andRightNameEqualTo(String value) {
            addCriterion("RIGHT_NAME =", value, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameNotEqualTo(String value) {
            addCriterion("RIGHT_NAME <>", value, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameGreaterThan(String value) {
            addCriterion("RIGHT_NAME >", value, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameGreaterThanOrEqualTo(String value) {
            addCriterion("RIGHT_NAME >=", value, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameLessThan(String value) {
            addCriterion("RIGHT_NAME <", value, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameLessThanOrEqualTo(String value) {
            addCriterion("RIGHT_NAME <=", value, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameLike(String value) {
            addCriterion("RIGHT_NAME like", value, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameNotLike(String value) {
            addCriterion("RIGHT_NAME not like", value, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameIn(List<String> values) {
            addCriterion("RIGHT_NAME in", values, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameNotIn(List<String> values) {
            addCriterion("RIGHT_NAME not in", values, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameBetween(String value1, String value2) {
            addCriterion("RIGHT_NAME between", value1, value2, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightNameNotBetween(String value1, String value2) {
            addCriterion("RIGHT_NAME not between", value1, value2, "rightName");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionIsNull() {
            addCriterion("RIGHT_DESCRIPTION is null");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionIsNotNull() {
            addCriterion("RIGHT_DESCRIPTION is not null");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionEqualTo(String value) {
            addCriterion("RIGHT_DESCRIPTION =", value, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionNotEqualTo(String value) {
            addCriterion("RIGHT_DESCRIPTION <>", value, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionGreaterThan(String value) {
            addCriterion("RIGHT_DESCRIPTION >", value, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("RIGHT_DESCRIPTION >=", value, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionLessThan(String value) {
            addCriterion("RIGHT_DESCRIPTION <", value, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionLessThanOrEqualTo(String value) {
            addCriterion("RIGHT_DESCRIPTION <=", value, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionLike(String value) {
            addCriterion("RIGHT_DESCRIPTION like", value, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionNotLike(String value) {
            addCriterion("RIGHT_DESCRIPTION not like", value, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionIn(List<String> values) {
            addCriterion("RIGHT_DESCRIPTION in", values, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionNotIn(List<String> values) {
            addCriterion("RIGHT_DESCRIPTION not in", values, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionBetween(String value1, String value2) {
            addCriterion("RIGHT_DESCRIPTION between", value1, value2, "rightDescription");
            return (Criteria) this;
        }

        public Criteria andRightDescriptionNotBetween(String value1, String value2) {
            addCriterion("RIGHT_DESCRIPTION not between", value1, value2, "rightDescription");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}