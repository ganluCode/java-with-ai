package cn.geekslife.immutable;

/**
 * 使用Builder模式的不可变人员类
 * 演示如何使用Builder模式创建复杂的不可变对象
 */
public final class ImmutablePersonWithBuilder {
    // 使用final关键字确保字段不可变
    private final String name;
    private final int age;
    private final String email;
    private final ImmutableAddress address;
    private final String phoneNumber;
    
    /**
     * 私有构造函数，只能通过Builder创建
     * @param builder Builder对象
     */
    private ImmutablePersonWithBuilder(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
    }
    
    /**
     * 获取姓名
     * @return 姓名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取年龄
     * @return 年龄
     */
    public int getAge() {
        return age;
    }
    
    /**
     * 获取邮箱
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 获取地址
     * @return 地址副本
     */
    public ImmutableAddress getAddress() {
        return address != null ? 
            new ImmutableAddress(address.getStreet(), address.getCity(), address.getZipCode()) : 
            null;
    }
    
    /**
     * 获取电话号码
     * @return 电话号码
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * 静态Builder类
     */
    public static class Builder {
        private String name;
        private int age;
        private String email;
        private ImmutableAddress address;
        private String phoneNumber;
        
        /**
         * 设置姓名
         * @param name 姓名
         * @return Builder实例
         */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * 设置年龄
         * @param age 年龄
         * @return Builder实例
         */
        public Builder setAge(int age) {
            this.age = age;
            return this;
        }
        
        /**
         * 设置邮箱
         * @param email 邮箱
         * @return Builder实例
         */
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }
        
        /**
         * 设置地址
         * @param address 地址
         * @return Builder实例
         */
        public Builder setAddress(ImmutableAddress address) {
            this.address = address;
            return this;
        }
        
        /**
         * 设置电话号码
         * @param phoneNumber 电话号码
         * @return Builder实例
         */
        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        
        /**
         * 构建ImmutablePersonWithBuilder对象
         * @return ImmutablePersonWithBuilder实例
         */
        public ImmutablePersonWithBuilder build() {
            // 验证必需字段
            if (name == null || name.isEmpty()) {
                throw new IllegalStateException("姓名是必需的");
            }
            
            if (age < 0) {
                throw new IllegalStateException("年龄不能为负数");
            }
            
            return new ImmutablePersonWithBuilder(this);
        }
    }
    
    /**
     * 获取Builder实例
     * @return Builder实例
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "ImmutablePersonWithBuilder{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}