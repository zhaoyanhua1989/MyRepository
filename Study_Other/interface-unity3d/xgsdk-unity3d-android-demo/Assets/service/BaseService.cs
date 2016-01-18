




namespace UnityDemoService
{
	public class BaseService
	{
		protected internal const string INTERFACE_TYPE_CREATE_ORDER = "create-order";
		protected internal const string INTERFACE_TYPE_UPDATE_ORDER = "update-order";
		protected internal const string INTERFACE_TYPE_CANCEL_ORDER = "cancel-order";
		protected internal const string INTERFACE_TYPE_TESTCHANNEL_NOTIFY = "testchannel-notify";
		protected internal const string INTERFACE_TYPE_QUERY_ORDER_STATUS = "query-order-status";
		protected internal const string INTERFACE_TYPE_VERIFY_SESSION = "verify-session";
		
		protected internal const int THREAD_JOIN_TIME_OUT = 30000;
		
		// 创建订单URI
		protected internal static string PAY_NEW_ORDER_URI = "/pay/create-order";
		// 更新订单URI
		protected internal static string PAY_UPDATE_ORDER_URI = "/pay/update-order";
		// 取消订单URI
		protected internal static string PAY_CANCEL_ORDER_URI = "/pay/cancel-order";
		
		protected internal static string PAY_TESTCHANNEL_NOTIFY = "/pay/testchannel-notify";
		
		protected internal static string PAY_QUERY_ORDER_STATUS = "/pay/query-order-status";
		
		protected internal static string ACCOUNT_VERIFY_SESSION_URI = "/account/verify-session/";
		
		private const string TIME_PATTERN = "yyyyMMddHHmmss";



	}
}
