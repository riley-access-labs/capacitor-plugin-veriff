import Foundation
import Capacitor
import Veriff

@objc(VeriffPluginPlugin)
public class VeriffPluginPlugin: CAPPlugin {
    private var call: CAPPluginCall!
    
    @objc func start(_ call: CAPPluginCall) {
        
        self.call = call
        let sessionUrl = call.getString("sessionUrl") ?? ""
        var configuration = VeriffSdk.Configuration()
        
        if let veriffConfiguration: Dictionary<String,String> = call.getObject("configuration") as? Dictionary<String,String> {
            let branding = VeriffSdk.Branding()
            branding.primary = UIColor.init(hexString: veriffConfiguration["primary"]!)
            branding.background = UIColor.init(hexString: veriffConfiguration["backgroundColor"]!)
            configuration = VeriffSdk.Configuration(branding: branding)
        }
        
        let veriff = VeriffSdk.shared
        veriff.delegate = self
        DispatchQueue.main.async {
            veriff.startAuthentication(sessionUrl: sessionUrl, configuration: configuration, presentingFrom: (self.bridge?.viewController!)!)
        }
    }
}

extension VeriffPluginPlugin : VeriffSdkDelegate {
    public func sessionDidEndWithResult(_ result: VeriffSdk.Result) {
        var resultJson: Dictionary<String,String> = [:]
        switch result.status {
        case .done:
            // The user successfully submitted the session
            resultJson["status"] = "DONE"
            resultJson["message"] = "Session is completed from clients perspective"
            break
        case .canceled:
            // The user canceled the verification process.
            resultJson["status"] = "CANCELED"
            resultJson["message"] = "User canceled the verification process"
            break
        case .error(let error):
            resultJson["status"] = "ERROR"
            resultJson["message"] = VeriffSdk.Error.init(rawValue: error.rawValue).debugDescription
            
        @unknown default:
            print("Unknown result received from Veriff SDK")
            return self.call.reject("Unknown result received from Veriff SDK")
        }
        
        self.call.resolve(resultJson)
    }
}
