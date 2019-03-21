package com.wade.dingtalk.domain;

public class TokenResult extends Result {


  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String refreshToken;
  private User user;

  private TokenResult(boolean success, int code, String message, Object detail) {
    super(success, code, message, detail);
  }

  public TokenResult(String accessToken, String refreshToken, User user) {
    super(true, 200, null, accessToken);
    this.refreshToken = refreshToken;
    this.user = user;

  }

  public static TokenResult clientError(String message) {
    return new TokenResult(false, 400, message, null);
  }


  public String getAccessToken() {
    return (String) this.getDetail();
  }

  public String getRefreshToken() {
    return refreshToken;
  }



  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }



  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((refreshToken == null) ? 0 : refreshToken.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    TokenResult other = (TokenResult) obj;
    if (refreshToken == null) {
      if (other.refreshToken != null)
        return false;
    } else if (!refreshToken.equals(other.refreshToken))
      return false;
    if (user == null) {
      if (other.user != null)
        return false;
    } else if (!user.equals(other.user))
      return false;
    return true;
  }

}
