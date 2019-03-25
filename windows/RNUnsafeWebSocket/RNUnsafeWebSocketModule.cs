using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Unsafe.Web.Socket.RNUnsafeWebSocket
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNUnsafeWebSocketModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNUnsafeWebSocketModule"/>.
        /// </summary>
        internal RNUnsafeWebSocketModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNUnsafeWebSocket";
            }
        }
    }
}
